# Java 基础

## Java 简介

### Java 运行过程

**java 文件**：在 idea 编写的 java 程序。

**.class 文件**：java 文件经过编译生成文件，运行在 jvm 上。不同 OS 会有对应的版本的 jvm，但 java 编译后的字节码文件是相同，因此只面向虚拟机。这就是 java 可以实现 **可移植性** 的原因。

**机器语言**：电脑可以识别的语言，字节码文件在虚拟机上解释产生。`.class->机器码` 这一步。在这一步 JVM 类加载器首先加载字节码文件，然后通过解释器逐行解释执行。这种方式的执行速度会相对比较慢。而且，有些方法和代码块是经常需要被调用的(也就是所谓的热点代码)，所以后面引进了 **JIT（Just in Time Compilation）** 编译器，而 JIT 属于运行时编译。当 JIT 编译器完成第一次编译后，其会将字节码对应的机器码保存下来，下次可以直接使用。而我们知道，机器码的运行效率肯定是高于 Java 解释器的。这也解释了我们为什么经常会说 **Java 是编译与解释共存的语言** 。

- **编译型**：**编译型语言** 会通过 **编译器** 将源代码一次性翻译成可被该平台执行的机器码。一般情况下，编译语言的执行速度比较快，开发效率比较低。常见的编译性语言有 C、C++、Go、Rust 等等。
- **解释型**：**解释型语言** 会通过 **解释器** 一句一句的将代码解释（interpret）为机器代码后再执行。解释型语言开发效率比较快，执行速度比较慢。常见的解释性语言有 Python、JavaScript、PHP 等等。
- 为什么经常会说 **Java 是编译与解释共存的语言**：因为 java 程序运行过程中，即需要编译器生成字节码文件，也需要解释器生成机器码。

### JVM&JDK&JRE

**JVM**：是运行 Java 字节码的虚拟机。**JVM 存在多个版本**。JVM 对于不同的 OS 有对应的版本，目的是使用相同的字节码，它们都会给出相同的结果。字节码和不同系统的 JVM 实现是 Java 语言 **一次编译，随处可以运行** 的关键所在。

**JDK**：供开发人员使用，包含 **JVM** 与 **基础类库**，以 **及开发调试工具**。

**JRE**：为运行 java 使用，包含 **JVM** 与 **基础类库**。

## JVM

参考文献

[JavaGuide]: https://javaguide.cn
[黑马在线 JVM 笔记]: https://lisxpq12rl7.feishu.cn/wiki/ZaKnwhhhmiDu9ekUnRNcv2iNnof

### 初始 JVM

JVM 本质上是一个运行在计算机上的程序，职责是运行 Java 字节码文件。

Java 源代码执行流程分为三个步骤：**编写 Java 源代码文件** → **使用 Java 编译器（javac 命令）将源代码编译成 Java 字节码文件** → **Java 虚拟机加载并运行 Java 字节码文件，此时会启动一个新的进程**。

#### JVM 的功能

1. **解释运行**：对字节码文件中的指令，实时的解释成机器码，让计算机执行。
2. **内存管理**：自动为对象、方法等分配内存。自动的垃圾回收机制，回收不再使用的对象（这是对比 C/C++语言需要手动回收内存
3. **即时编译**：对热点代码进行优化，提升执行效率。即时编译可以说是提升 Java 程序性能最核心的手段。

#### JVM 的组成

1. **类加载器**：核心组件类加载器，负责将字节码文件中的内容加载到内存中
2. **运行时数据区**：JVM 管理的内存，创建出来的对象、类的信息等等内容都会放在这块区域中
3. **执行引擎**：包含了即时 **编译器**、**解释器**、**垃圾回收器**，执行引擎使用解释器将字节码指令解释成机器码，使用即时编译器优化性能，使用垃圾回收器回收不再使用的对象。
4. **本地接口**：调用本地使用 C/C++编译好的方法，本地方法在 Java 中声明时，都会带上 **native 关键字**

### 字节码文件详解

二进制的方式存储

#### 字节码文件结构

- **基础信息**：魔数、字节码文件对应的 Java 版本号、访问标识(public final 等等)、父类和接口信息
- **常量池**： 保存了字符串常量、类或接口名、字段名，主要在字节码指令中使用
- **字段：** 当前类或接口声明的字段信息
- **方法：** 当前类或接口声明的方法信息，核心内容为方法的字节码指令
- **属性：** 类的属性，比如源码的文件名、内部类的列表等

```java
ClassFile {
    u4             magic; //Class 文件的标志
    u2             minor_version;//Class 的小版本号
    u2             major_version;//Class 的大版本号
    u2             constant_pool_count;//常量池的数量
    cp_info        constant_pool[constant_pool_count-1];//常量池
    u2             access_flags;//Class 的访问标记
    u2             this_class;//当前类
    u2             super_class;//父类
    u2             interfaces_count;//接口数量
    u2             interfaces[interfaces_count];//一个类可以实现多个接口
    u2             fields_count;//字段数量
    field_info     fields[fields_count];//一个类可以有多个字段
    u2             methods_count;//方法数量
    method_info    methods[methods_count];//一个类可以有个多个方法
    u2             attributes_count;//此类的属性表中的属性数
    attribute_info attributes[attributes_count];//属性表集合
}
```

1. **魔数**：每个 Class 文件的头 4 个字节，而且是固定的。**确定这个文件是否为一个能被虚拟机接收的 Class 文件**。Java 规范规定魔数为固定值：0xCAFEBABE。如果读取的文件不是以这个魔数开头，Java 虚拟机将拒绝加载它。

2. **主副版本号**：第 5 和第 6 个字节是 **次版本号**，第 7 和第 8 个字节是 **主版本号**。

3. **常量池**：常量池的数量是 `constant_pool_count-1`（**常量池计数器是从 1 开始计数的，将第 0 项常量空出来是有特殊考虑的，索引值为 0 代表“不引用任何一个常量池项”**）。

   常量池主要存放两大常量：**字面量** 和 **符号引用**。字面量比较接近于 Java 语言层面的的常量概念，如文本 **字符串**、声明为 **final 的常量** 值等。而符号引用则属于编译原理方面的概念。包括下面三类常量：

   - 类和接口的全限定名

   - 字段的名称和描述符

   - 方法的名称和描述符

4. **字段**：字段中存放的是当 **前类或接口声明的字段信息**(字段信息就是一些声明变量语句），如字段的名字、描述符（字段的类型）、访问标识（public/private static final 等）。字段包括类级变量以及实例变量，但不包括在 **方法内部声明的局部变量**。

   ```java
   public int name;
   finall public doubele age = 10;
   Student stu = new Student(10)
   ```

5. **方法**：方法区域是存放 **字节码指令** 的核心位置，字节码指令的内容存放在方法的 Code 属性中。

   <img src="D:\Document\JAVA\images\54a4ef3c-ce98-496f-ae23-a44646b9bf6c.png" style="zoom:50%;" />

   <img src="D:\Document\JAVA\images\a2e987f8-3225-4d4f-a17f-77ecbf48b827.png" alt="a2e987f8-3225-4d4f-a17f-77ecbf48b827" style="zoom:50%;" />

6. **属性**：属性主要指的是类的属性，比如源码的文件名、内部类的列表等

### 类的生命周期

![类的生命周期](D:\Document\JAVA\images\a863b68f-da90-4fee-8455-58b2e5a2d616.png)

#### 加载

通过全类名获取定义此类的二进制字节流。

Java 虚拟机会将字节码中的信息保存到方法区中，方法区中生成一个 InstanceKlass 对象，保存类的所有信息，里边还包含实现特定功能比如多态的信息。

Java 虚拟机同时会在堆上生成与方法区中数据类似的 java.lang.Class 对象，作用是在 Java 代码中去获取类的信息以及存储静态字段的数据（JDK8 及之后）(作用：反射，管理)

#### 验证

Class 文件的字节流中包含的信息符合《Java 虚拟机规范》的全部约束要求

1、文件格式验证，比如文件是否以 0xCAFEBABE 开头，主次版本号是否满足当前 Java 虚拟机版本要求。

2、元信息验证，例如类必须有父类（super 不能为空）。

3、验证程序执行指令的语义，比如方法内的指令执行中跳转到不正确的位置。

4、符号引用验证，例如是否访问了其他类中 private 的方法等。

#### 准备

准备阶段为静态变量（static）分配内存并设置初值，每一种基本数据类型和引用数据类型都有其初值。

```Java
public class Student{
    public static int value = 1;
    // 对于第一个语句，vlaue在准备阶段先赋予int类型的初始值0，之后在初始化阶段在改为1
    public static final int value2 = 2;
    // 特殊情况下有final修饰的静态变量，直接在初始阶段就赋值为2.

}
```

#### 解析

虚拟机将常量池内的符号引用替换为直接引用的过程，符号引用是在 **字节码文件中使用 index** 来访问常量池中的内容，转化为 **直接使用内存中地址** 进行访问具体的数据。

#### 初始化

`init` 与 `clinit`：前者是构造方法，在对象初始化时运行，后者在类的初始化阶段运行。

```java
class X {
    static Log log = LogFactory.getLog(); // <clinit>
    private int x = 1;   // <init> 没有static
    X(){
        // <init> 对象的构造方法
    }
    static {
        // <clinit>
    }
}
```

因此只有一下情况才是 `clinit`：

1. 访问一个类的静态变量或者静态方法，注意变量是 final 修饰的并且等号右边是常量不会触发初始化。
2. 调用 Class.forName(String className)。
3. new 一个该类的对象时。
4. 执行 Main 方法的当前类。

##### 面试题 1

如下代码的输出结果是什么？

```Java
public class Test1 {
    public static void main(String[] args) {
        System.out.println("A");
        new Test1();
        new Test1();
    }

    public Test1(){
        System.out.println("B");
    }

    {
        System.out.println("C");
    }

    static {
        System.out.println("D");
    }
}
```

分析步骤：

1、**执行 main 方法之前，先执行 clinit 指令**。指令会 **输出 D**

2、执行 main 方法的字节码指令。指令会 **输出 A**

3、创建两个对象，会执行两次对象初始化的指令。这里会 **输出 CB**，源代码中输出 C 这行，被放到了对象初始化的一开始来执行。

所以最后的结果应该是 **DACBCB**

**clinit 不会执行的几种情况**，如下几种情况是不会进行初始化指令执行的：

1.无静态代码块且无静态变量赋值语句。

2.有静态变量的声明，但是没有赋值语句。`public static int a;`

3.静态变量的定义使用 **final 关键字**，这类变量会在准备阶段直接进行初始化。`public  final static int a;`

##### 面试题 2

**直接访问父类的是静态变量。不会触发子类的初始化**，**子类的初始化 `clinit` 之前，会先调用父类的 `clinit`**

```Java
public class Demo01 {
    public static void main(String[] args) {
        new B02();
        System.out.println(B02.a);
    }
}

class A02{
    static int a = 0;
    static {
        a = 1;
    }
}

class B02 extends A02{
    static {
        a = 2;
    }
}
```

分析步骤：

1、调用 new 创建对象，需要初始化 B02，优先初始化父类。

2、执行 A02 的初始化代码，将 a 赋值为 1。

3、**B02 初始化，将 a 赋值为 2**。

**变化**：

将 `new B02();` 注释掉会怎么样？

分析步骤：

1、访问父类的静态变量，只初始化父类。

2、**执行 A02 的初始化代码，将 a 赋值为 1**。

##### 补充练习题

分析如下代码执行结果:

```Java
public class Test2 {
    public static void main(String[] args) {
        Test2_A[] arr = new Test2_A[10];

    }
}
class Test2_A {
    static {
        System.out.println("Test2 A的静态代码块运行");
    }
}
// 无输出
```

**数组的创建不会导致数组中元素的类进行初始化**。

```Java
public class Test4 {
    public static void main(String[] args) {
        System.out.println(Test4_A.a);
    }
}
class Test4_A {
    public static final int a = Integer.valueOf(1);

    static {
        System.out.println("Test3 A的静态代码块运行");
    }
}
// 输出
// Test3 A的静态代码块运行
// 1
```

**final 修饰的变量如果赋值的内容需要执行指令才能得出结果，会执行 clinit 方法进行初始化。**

### 类的加载器

本质就是 **加载 Java 类的字节码 `.class` 文件到 JVM 中，在内存中生成一个代表该类的 `Class` 对象**

#### 分类

1. **jvm 底层(c++)** 实现的类加载器：启动类加载器 **Bootstrap**，其负责加载底层的类，比如 String 类。**通常表示为 null**，并且 **没有父级**，主要用来加载 JDK 内部的核心类库（ `%JAVA_HOME%/lib` 目录下的 jar 包和类）以及被 `-Xbootclasspath` 参数指定的路径下的所有类。

   **问题 1**：为什么 `getClassLoader()` 表示为 null？这是因为 `BootstrapClassLoader` 由 C++ 实现，由于这个 C++ 实现的类加载器在 Java 中是没有与之对应的类的，所以拿到的结果是 null。因此 **获取到 `ClassLoader` 为 `null` 就是 `BootstrapClassLoader` 加载**。

   **问题 2**：可以手动添加 Bootstrap 吗？可以手动添加，方法一在 `%JAVA_HOME%/lib` 目录下添加自己的包。方法二使用 `-Xbootclasspath/a:jar` 包目录/jar 包名 进行扩展，参数中的/a 代表新增。

2. **`ExtensionClassLoader`(扩展类加载器)**：主要负责加载 `%JRE_HOME%/lib/ext` 目录下的 jar 包和类以及被 `java.ext.dirs` 系统变量所指定的路径下的所有类。

3. **`AppClassLoader`(应用程序类加载器)**：面向我们用户的加载器，负责加载当前应用 classpath 下的所有 jar 包和类。

   **注 1**：扩展类加载器和应用程序类加载器都是 JDK 中提供的、使用 Java 编写的类加载器。而且均继承于 `ClassLoader`。
   
   **注 2**：**每个类加载器都有一个父类加载器**。**启动类加载器没有父类加载器**

#### 双亲委派机制

![双亲委派机制](D:\Document\JAVA\images\59e7bf8f-ebfa-45b6-a22c-26c076446e62.png)

双亲委派机制是指：在加载一个类时，先交给父类检查是否加载过，即 **向上查找是否加载过**。如果已经加载就不会重复加载，若没有加载，则会检查这个类是否在自己的目录下，如果在则加载，如果不在则交给子类加载，即 **向下尝试加载**。

举个例子，B 类在扩展类加载器加载路径中。应用程序加载器会先检查是否加载过，没有则按顺序交给扩展类加载器，启动类加载器检查，发现都没有加载过。之后启动类加载器尝试加载，发现 b 不在自己的目录下，交给扩展类加载器加载，扩展类加载器发现 b 在自己的目录下，则进行加载。

几个问题

- 双亲委派的作用？答：**1.保证类加载的安全性。通过双亲委派机制避免恶意代码替换 JDK 中的核心类库，比如 java.lang.String，确保核心类库的完整性和安全性。2.避免重复加载。双亲委派机制可以避免同一个类被多次加载**。
- 如果一个类重复出现在三个类加载器的加载位置，应该由谁来加载？答：**启动类加载器加载，根据双亲委派机制，它的优先级是最高的**。
- String 类能覆盖吗，在自己的项目中去创建一个 java.lang.String 类，会被加载吗？答：**不能，会返回启动类加载器加载在 rt.jar 包中的 String 类**。

#### 打破双亲委派机制

打破双亲委派机制历史上有三种方式，但本质上只有 **第一种算是真正的打破了双亲委派机制**：

- **自定义类加载器并且重写 loadClass 方法**。Tomcat 通过这种方式实现应用之间类隔离。
- **线程上下文类加载器**。利用上下文类加载器加载类，比如 JDBC 和 JNDI 等。
- Osgi 框架的类加载器。历史上 Osgi 框架实现了一套新的类加载器机制，允许同级之间委托进行类的加载，目前很少使用。

几个问题

- 为什么要破坏双亲委派？如何破坏？答：可以局对应的例子，Tomcat 与 JDBC。
- 为什么是重写 `loadClass()` 方法打破双亲委派模型呢？答：重写 `loadClass()` 方法之后，我们就可以改变传统双亲委派模型的执行流程。例如，子类加载器可以在委派给父类加载器之前，先自己尝试加载这个类，或者在父类加载器返回之后，再尝试从其他地方加载这个类。具体的规则由我们自己实现，根据项目需求定制化

##### 自定义类加载器

一个 Tomcat 程序中是可以运行多个 Web 应用的，如果这两个应用中出现了相同限定名的类，比如 Servlet 类，Tomcat 要保证这两个类都能加载并且它们应该是不同的类。如果不打破双亲委派机制，当应用类加载器加载 Web 应用 1 中的 MyServlet 之后，Web 应用 2 中相同限定名的 MyServlet 类就无法被加载了。Tomcat 使用了 **自定义类加载器来实现应用之间类的隔离**。 **每一个应用会有一个独立的类加载器加载对应的类**。

ClassLoader 中包含了 4 个核心方法，双亲委派机制的核心代码就位于 loadClass 方法中。

```Java
public Class<?> loadClass(String name)
// 类加载的入口，提供了双亲委派机制。内部会调用findClass   重要
protected Class<?> findClass(String name)
// 由类加载器子类实现,获取二进制数据调用defineClass ，比如URLClassLoader会根据文件路径去获取类文件中的二进制数据。重要
protected final Class<?> defineClass(String name, byte[] b, int off, int len)
// 做一些类名的校验，然后调用虚拟机底层的方法将字节码信息加载到虚拟机内存中
protected final void resolveClass(Class<?> c)
// 执行类生命周期中的连接阶段
```

怎么使用自定义类实现打破双亲机制？答：重写 `loadClass`。举个例子：

```java
public class BreakClassLoader1 extends ClassLoader { // ClassLoader是个抽象类，继承重写即可

    private String basePath;
    private final static String FILE_EXT = ".class";

    //设置加载目录
    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    //使用commons io 从指定目录下加载文件
    private byte[] loadClassData(String name)  {
        try {
            String tempName = name.replaceAll("\\.", Matcher.quoteReplacement(File.separator));
            FileInputStream fis = new FileInputStream(basePath + tempName + FILE_EXT);
            try {
                return IOUtils.toByteArray(fis);
            } finally {
                IOUtils.closeQuietly(fis);
            }

        } catch (Exception e) {
            System.out.println("自定义类加载器加载失败，错误原因：" + e.getMessage());
            return null;
        }
    }

    //重写loadClass方法
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        //如果是java包下，还是走双亲委派机制
        if(name.startsWith("java.")){
            return super.loadClass(name);
        }
        //从磁盘中指定目录下加载
        byte[] data = loadClassData(name);
        //调用虚拟机底层方法，方法区和堆区创建对象
        return defineClass(name, data, 0, data.length);
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        //第一个自定义类加载器对象
        BreakClassLoader1 classLoader1 = new BreakClassLoader1();
        classLoader1.setBasePath("D:\\lib\\");

        Class<?> clazz1 = classLoader1.loadClass("com.itheima.my.A");
         //第二个自定义类加载器对象
        BreakClassLoader1 classLoader2 = new BreakClassLoader1();
        classLoader2.setBasePath("D:\\lib\\");

        Class<?> clazz2 = classLoader2.loadClass("com.itheima.my.A");

        System.out.println(clazz1 == clazz2);

        Thread.currentThread().setContextClassLoader(classLoader1);

        System.out.println(Thread.currentThread().getContextClassLoader());

        System.in.read();
     }
}
```

几个问题

- 自定义加载器的父类为什么是应用类加载器？答：默认情况下自定义类加载器的父类加载器是应用程序类加载器。
- 不用加载但相同名为什么不冲突？答：不会冲突，在同一个 Java 虚拟机中，只有相同类加载器+相同的类限定名才会被认为是同一个类。

##### 上下文线程

单纯依靠自定义类加载器没办法满足某些场景的要求，例如，有些情况下，高层的类加载器需要加载低层的加载器才能加载的类。

比如，SPI 中，SPI 的接口（如 `java.sql.Driver`）是由 Java 核心库提供的，由 `BootstrapClassLoader` 加载。而 SPI 的实现（如 `com.mysql.cj.jdbc.Driver`）是由第三方供应商提供的，它们是由应用程序类加载器或者自定义类加载器来加载的。默认情况下，一个 **类及其依赖类由同一个类加载器加载**。所以，加载 SPI 的接口的类加载器（`BootstrapClassLoader`）也会用来加载 SPI 的实现。按照双亲委派模型，`BootstrapClassLoader` 是无法找到 SPI 的实现类的，因为它无法委托给子类加载器去尝试加载。

线程上下文类加载器的原理是将一个 **类加载器保存在线程私有数据** 里，跟线程绑定，然后在需要的时候取出来使用。

还是上面的数据库例子，先由 **启动类加载器** 加载 SPI 的接口，之后在通过 **线程上下文加载器**，加载保存好的 SPI 实现类。

### 运行时数据区

<img src="D:\Document\JAVA\images\neicun-jiegou-20240110195211.png" alt="版本对比" style="zoom:50%;" />

在 1.6 之前，常量池不区分 **字符串常量池** 与 **运行时常量池**，统一归为 **常量池**，放在 **方法区，即永久代**。

在 1.7 版本，将常量池分开为 **字符串常量池存储在堆**，**运行时常量池存储在方法区**

在 1.8 之后，**方法区改为元空间**，对应位置不变。但 1.7 之前，**方法区在运行时数据区**，1.8 之后 **元空间在本地内存**（与直接内存在一起）

运行时数据区主要分为 **线程私有：虚拟机栈，本地方法栈，程序计数器**；以及 **线程共享：方法区，堆**。

#### 程序计数器

通计组当中的 PC 寄存器，分支、循环、跳转、异常处理、线程恢复等功能。**不存在内存溢出**。

#### 虚拟机栈

Java 虚拟机栈随着线程的创建而创建，而回收则会在线程的销毁时进行。由于方法可能会在不同线程中执行，**每个线程都会包含一个自己的虚拟机栈**。每一个方法的调用使用一个 **虚拟机栈帧**。

其中虚拟机栈包含：

- 局部变量表，局部变量表的作用是在运行过程中存放所有的 **局部变量**。局部变量表保存的内容有：实例 **方法的 this 对象**（位于 0），方法的 **参数**（紧跟 this），方法体中 **声明的局部变量**。在栈帧中，局部变量表是以数组形式，一个槽存放一个内容。例如，下段代码用几个槽。分析：首先一定有方法的 this，参数 k，m。因此占用 3 个槽 0~2。之后是第一个局部变量，a、b。由于 a、b 与 c 不在一个局部当中，因此 c 会占用 a 的位置。之后 i、j 也会占用之前局部的，但由于 j 是 long 型，因此要占用两个。所以是 **6 个槽**。

  ```java
  public void test4(int k,int m){
      // this k m
      {
          int a = 1;
          int b = 2;
      }
      // this k m a b
      {
          int c = 1;
      }
      // this k m c b
      int i = 0;
      long j = 1;
      // this k m i j j
  }
  ```

- 操作数栈，操作数栈是栈帧中虚拟机在执行指令过程中用来存放 **临时数据** 的一块区域。
- 帧数据，帧数据主要包含动态链接、方法出口、异常表的引用

注意：**Java 虚拟机栈存在溢出的情况**。

#### 本地方法栈

本地方法栈存储的是 **native 本地方法** 的栈帧。

#### 堆

Java 程序中堆内存是空间最大的一块内存区域。**线程共享**。**创建出来的对象**（即 **使用 new 关键字**）都存在于堆上。栈上的局部变量表中，可以存放堆上 **对象的引用**（即 **栈帧中的局部变量存放的是一个地址，指向堆的一块空间**）。**静态变量** 也可以存放堆对象的引用，通过静态变量就可以实现对象在线程之间共享。

堆同样存在 **内存溢出** 的情况（不断 new 或声明静态变量）。

堆空间有三个需要关注的值，used、total、max。used 指的是当前已使用的堆内存，total 是 java 虚拟机已经分配的可用堆内存，max 是 java 虚拟机可以分配的最大堆内存。

#### 方法区

方法区是存放基础信息的位置，**线程共享**，主要包含三部分内容：

- 类的元信息，保存了所有类的基本信息。在类的 **加载阶段** 完成
- 运行时常量池，保存了字节码文件中的常量池内容。字节码文件中通过编号查表的方式找到常量，这种常量池称为静态常量池。当常量池加载到内存中之后，可以通过内存地址快速的定位到常量池中的内容，这种常量池称为运行时常量池。
- 字符串常量池，保存了字符串常量。定义的常量字符串内容往往放进字符串常量池。

#### 字符串常量池

只有通过 `"xxx"` 直接声明的才存放在字符串常量池。而通过 `new String("xx")` 本质是 `new`，因此存放在堆当中。所以下段代码输出 `false`。

```java
String str1 = "abc";
String str2 = new String("abc");
System,out,print(str1 == str2)
// == 比较地址，而equal比较内容
```

其次，如果字符串常量池中已经存在，在直 **接声明一个相同的串，不会开辟新空间，而 new 会在堆中开一个新的空间**

**拼接问题**：两个变量使用 `+` 进行拼接本质是使用 new 一个 StringBuilder，而两个常量使用 `+` 进行拼接会进行优化。

`intern()` 方法的主要作用是确保字符串引用在常量池中的唯一性。当调用 `intern()` 时，**如果常量池中已经存在相同内容的字符串，则返回常量池中已有对象的引用；否则，将该字符串添加到常量池并返回其引用。**

```java
String s1 = "a";
String s2 = "b";
String s3 = s1+s2;
String s5="a"+"b"; // 直接优化为ab，直接将ab存放在字符串常量池
String s4 = "ab"; // 字符串常量池中已经存在s5的ab，直接引用相同地址
System.out.println(s5==s4);//true
System.out.println(s3==s4);//false
```

### 垃圾回收

JAVA 中引入自动垃圾回收

- 自动垃圾回收，自动根据对象是否使用由虚拟机来回收对象
  - 优点：降低程序员实现难度、降低对象回收 bug 的可能性
  - 缺点：程序员无法控制内存回收的及时性
- 手动垃圾回收，由程序员编程实现对象的删除
  - 优点：回收及时性高，由程序员把控回收的时机
  - 缺点：编写不当容易出现悬空指针、重复释放、内存泄漏等问题

#### 回收对象判断

在 c/c++中主要使用 **引用计数法**，引用计数法会为每个对象维护一个引用计数器，当对象被引用时加 1，取消引用时减 1。引用计数法的优点是实现简单，C++中的智能指针就采用了引用计数法，但是它也存在缺点：1.**每次引用和取消引用都需要维护计数器**，对系统性能会有一定的影响 2.存在 **循环引用问题**，所谓循环引用就是当 A 引用 B，B 同时引用 A 时会出现对象无法回收的问题。

在 JAVA 中主要使用 **可达性分析算法** 来判断对象是否可以被回收。这个算法的基本思想就是通过一系列的称为 **GC Roots** 的对象作为起点，从这些节点开始向下搜索，节点所走过的路径称为引用链，当一个对象到 GC Roots 没有任何引用链相连的话，则证明此对象是不可用的，需要被回收。

**哪些对象可以作为 GC Roots 呢？**

- **线程** Thread 对象，引用线程 **栈帧中的方法参数、局部变量** 等
- **系统类加载器** 加载的 java.lang.Class 对象，引用类中的 **静态变量**。
- 监视器对象，用来保存同步锁 synchronized 关键字持有的对象。
- 本地方法调用时使用的全局对象。

**对象可以被回收，就代表一定会被回收吗？** 即使在可达性分析法中不可达的对象，也并非是“非死不可”的，这时候它们暂时处于“缓刑阶段”，要真正宣告一个对象死亡，至少要经历两次标记过程；可达性分析法中不可达的对象被第一次标记并且进行一次筛选，筛选的条件是此对象是否有必要执行 `finalize` 方法。当对象没有覆盖 `finalize` 方法，或 `finalize` 方法已经被虚拟机调用过时，虚拟机将这两种情况视为没有必要执行。被判定为需要执行的对象将会被放在一个队列中进行第二次标记，除非这个对象与引用链上的任何一个对象建立关联，否则就会被真的回收。

#### 常见引用对象

- 软引用：软引用相对于强引用是一种比较弱的引用关系，如果一个对象只有软引用关联到它，当 **程序内存不足** 时，就会将软引用中的数据进行回收。**软引用对象本身，也需要被强引用，否则软引用对象也会被回收掉**
- 弱引用：弱引用的整体机制和软引用基本一致，区别在于弱引用包含的对象在垃圾回收时，不管 **内存够不够都** 会直接被回收。
- 虚引用：

#### 垃圾回收算法

**标记-清除算法**：首先 **标记** 出所有不需要回收的对象（如 **Java 的可达性分析算法**），在标记完成后统一 **回收掉所有没有被标记的对象**。它是最基础的收集算法，后续的算法都是对其不足进行改进得到。这种垃圾收集算法会带来两个明显的问题：

1. **效率问题**：标记和清除两个过程效率都不高。
2. **空间问题**：标记清除后会产生大 **量不连续的内存碎片**

**复制算法**：1.准备 **两块空间** From 空间和 To 空间，每次在对象 **分配阶段**，只能使用其中一块空间（From 空间）。2.在垃圾 **回收 GC 阶段**，将 **From 中存活对象复制到 To 空间**。在垃圾回收阶段，如果对象 A 存活，就将其复制到 To 空间。然后将 **From 空间直接清空**。3.将两块空间的 From 和 To 名字互换。缺点如下：

- **可用内存变小**：可用内存缩小为原来的一半。
- **不适合老年代**：如果存活对象数量比较大，复制性能会变得很差。

**标记-整理** 算法是根据老年代的特点提出的一种标记算法，标记过程仍然与“标记-清除”算法一样，但后续步骤不是直接对可回收对象回收，而是让所有存活的对象向一端移动，然后直接清理掉端边界以外的内存。即多出来一个 **整理碎片的步骤**。由于多了整理这一步，因此效率也不高，适合老年代这种垃圾回收频率不是很高的场景。

**分代垃圾回收算法**：将上述算法进行整合。分代垃圾回收将整个内存区域划分为 **年轻代** 和 **老年代**。比如在新生代中，每次收集都会有大量对象死去，所以可以选择“复制”算法，只需要付出少量对象的复制成本就可以完成每次垃圾收集。而老年代的对象存活几率是比较高的，而且没有额外的空间对它进行分配担保，所以我们必须选择“标记-清除”或“标记-整理”算法进行垃圾收集。

#### GC 回收器

垃圾回收器是垃圾回收算法的具体实现。由于垃圾回收器分为年轻代和老年代，除了 G1 之外其他垃圾回收器必须成对组合进行使用。



## Java 基础语法

### 注释

 注释是对代码的解释和说明文字。

Java 中的注释分为三种：

- 单行注释：

```java
// 这是单行注释文字
```

- 多行注释：

```java
/*
这是多行注释文字
这是多行注释文字
*/
```

- 文档注释（暂时用不到）：

```java
/**
这是多行注释文字
这是多行注释文字
*/
```

### 关键字

| **abstract**   | **assert**       | **boolean**   | **break**      | **byte**   |
| -------------- | ---------------- | ------------- | -------------- | ---------- |
| **case**       | **catch**        | **char**      | **class**      | **const**  |
| **continue**   | **default**      | **do**        | **double**     | **else**   |
| **enum**       | **extends**      | **final**     | **finally**    | **float**  |
| **for**        | **goto**         | **if**        | **implements** | **import** |
| **instanceof** | **int**          | **interface** | **long**       | **native** |
| **new**        | **package**      | **private**   | **protected**  | **public** |
| **return**     | **strictfp**     | **short**     | **static**     | **super**  |
| **switch**     | **synchronized** | **this**      | **throw**      | **throws** |
| **transient**  | **try**          | **void**      | **volatile**   | **while**  |

#### static

1. **修饰变量**
   - 修饰成员变量，说明这个成员变量是属于类的，这个成员变量称为 **类变量** 或者 **静态成员变量**。直接用 **类名访问** 即可。
2. **修饰方法**
   - 修饰成员方法， 直接用 类名访问即可。因为类只有一个，所以静态方法在内存区域中也只存在一份。所有的对象都可以共享这个方法。
3. **修饰类**
   - 一般是指 **静态内部类**。后续详解。

#### final

1. **修饰变量**
   - 基本类型：**局部变量** 必须 **在使用之前赋值一次才可以被使用**。**成员变量** 在声明时没有赋值的叫做 **空白 final 变量**。
   - 引用类型：**final 保证的是内存地址的指针指向不会改变。**
2. **修饰方法**
   - 被 final 修饰过的方法 **不可以在子类当中被重写**，但是可以被 **覆盖**（优先调用子类当中的重复方法，隐藏父类的方法）
3. **修饰类**
   - final 修饰过的类表示该类 **无法被任何其它的类继承**
   - final 类中的成员变量可以根据需要设为 final
   - 但是 final 类中所有的成员方法都会被 **隐式地指定为 final 方法**。

### 基本数据类型

 Java 语言数据类型的分类

- 基本数据类型
- 引用数据类型（面向对象的时候再深入学习）

#### 基本数据类型的四类八种

| 数据类型 | 关键字  | 内存占用 |                 取值范围                  |
| :------: | :-----: | :------: | :---------------------------------------: |
|   整数   |  byte   |    1     |    负的 2 的 7 次方 ~ 2 的 7 次方-1(-128~127)    |
|          |  short  |    2     | 负的 2 的 15 次方 ~ 2 的 15 次方-1(-32768~32767) |
|          |   int   |    4     |        负的 2 的 31 次方 ~ 2 的 31 次方-1        |
|          |  long   |    8     |        负的 2 的 63 次方 ~ 2 的 63 次方-1        |
|  浮点数  |  float  |    4     |        1.401298e-45 ~ 3.402823e+38        |
|          | double  |    8     |      4.9000000e-324 ~ 1.797693e+308       |
|   字符   |  char   |    2     |                  0-65535                  |
|   布尔   | boolean |    1     |                true，false                |

#### 需要记忆以下几点

byte 类型的取值范围：-128 ~ 127

int 类型的大概取值范围： -21 亿多 ~ 21 亿多

整数类型和小数类型的取值范围大小关系：double > float > long > int > short > byte

最为常用的数据类型选择：

- 在定义变量的时候，要根据实际的情况来选择不同类型的变量。

  比如：人的年龄，可以选择 byte 类型。

  比如：地球的年龄，可以选择 long 类型。

- 如果整数类型中，不太确定范围，那么默认使用 int 类型。

- 如果小数类型中，不太确定范围，那么默认使用 double 类型。

- 如果要定义字符类型的变量，使用 char

- 如果要定义布尔类型的变量，使用 boolean

- 如果要定义一个 long 类型的变量，那么在数据值的后面需要 **加上 L 后缀**。**否则将作为整型解析**。
- 如果要定义一个 float 类型的变量，那么在数据值的后面需要 **加上 F 后缀**。**否则将无法通过编译**。

#### 如何解决浮点数精度丢失

使用 `BigInteger` 与 `BigDecimal`

```java
BigInteger bi = new BigInteger("1234567890"); // 不变类
BigInteger i2 = new BigInteger("12345678901234567890");
BigInteger sum = i1.add(i2); // 12345678902469135780 继承自Number 可使用对应的方法进行算术运算
System.out.println(bi.longValue()); // 1234567890 BigInteger转换成基本类型时可使用longValueExact()
```

```java
BigDecimal d1 = new BigDecimal("123.4560"); // tripTrailingZeros()方法可以去末尾的0
System.out.println(d1.scale()); // 4,两位小数
BigDecimal d2 = new BigDecimal("23.456789");
BigDecimal d3 = d1.divide(d2, 10, RoundingMode.HALF_UP); // 保留10位小数并四舍五入
BigDecimal d4 = d1.divide(d2); // 报错：ArithmeticException，因为除不尽
BigDecimal c1 = new BigDecimal("123.456");
BigDecimal c2 = new BigDecimal("123.45600"); // 比较必须使用compareTo()方法
System.out.println(c1.equals(c2)); // false,因为scale不同
System.out.println(c1.equals(c2.stripTrailingZeros())); // true,因为d2去除尾部0后scale变为3
System.out.println(c1.compareTo(c2)); // 0 = 相等, -1 = d1 < d2, 1 = d1 > d2
```

### 包装数据类型

包装器类型（Wrapper Types）是 Java 中的一种特殊类型，用于将基本数据类型（如 int、float、char 等）转换为对应的引用类型。

Java 提供了以下包装器类型，与基本数据类型一一对应：

- Byte（对应 byte）
- Short（对应 short）
- Integer（对应 int）
- Long（对应 long）
- Float（对应 float）
- Double（对应 double）
- Character（对应 char）
- Boolean（对应 boolean）

```java
Integer integerValue = new Integer(42);
Integer n = Integer.valueOf(10); // 推荐 方法2 本质是静态工厂方法 创建新对象时，优先选用静态工厂方法而不是new操作符。
```

包装类型是 **不变类**，在源码中可以看出被 `final` 修饰，因此一旦创建不可以改变。

对两个 `Integer` 实例进行比较要特别注意：绝对不能用 `==` 比较，因为 `Integer` 是引用类型，必须使用 `equals()` 比较：

```java
Integer x = 127;
Integer y = 127;
Integer m = 99999;
Integer n = 99999;
System.out.println("x == y: " + (x==y)); // true
System.out.println("m == n: " + (m==n)); // false
System.out.println("x.equals(y): " + x.equals(y)); // true
System.out.println("m.equals(n): " + m.equals(n)); // true
```

问题：为什么都是 `==`，有的是 `true` 有的 `false`？答：`Integer.valueOf()` 对于较小的数（**缓存池**），始终返回相同的实例，因此，`==` 比较“恰好”为 `true`。这是为什么上面推荐方法二。因为方法 1 总是创建新的 `Integer` 实例，方法 2 把内部优化留给 `Integer` 的实现者去做，即使在当前版本没有优化，也有可能在下一个版本进行优化。

#### 与基本类型区别

**用途**：除了定义一些常量和局部变量之外，我们在其他地方比如方法参数、对象属性中很少会使用基本类型来定义变量。并且，**包装类型可用于泛型**，而基本类型不可以。

**存储方式**：**基本数据类型的局部变量存放在 Java 虚拟机栈中的局部变量表中，基本数据类型的成员变量（未被 `static` 修饰 ）存放在 Java 虚拟机的堆中**。包装类型属于对象类型，我们知道几乎所有对象实例都存在于堆中。

**占用空间**：相比于包装类型（对象类型）， 基本数据类型占用的空间往往非常小。

**默认值**：成员变量 **包装类型不赋值就是 `null`** ，而基本类型有默认值且不是 `null`。

**比较方式**：对于基本数据类型来说，`==` 比较的是值。对于包装数据类型来说，`==` 比较的是对象的内存地址。所有整型包装类对象之间值的比较，全部使用 `equals()` 方法。

#### 拆箱与装箱

- **装箱**：将基本类型用它们对应的引用类型包装起来；
- **拆箱**：将包装类型转换为基本数据类型；

```java
Integer i = 10;  //装箱 10是int基本类型，直接赋值给Integer包装
int n = i;   //拆箱 将一个包装类型Integer转化为基本类型int
// Integer i = 10 等价于 Integer i = Integer.valueOf(10)
// int n = i 等价于 int n = i.intValue();
```

## 数组

### 声明

```java
int[] anArray;
int anOtherArray[];
```

### 数组初始化

**静态初始化**，一旦构建，**长度无法改变**，并且需要提前设置值

```java
// 数据类型[] 数组名 = new 数据类型[]{元素1，元素2，元素3，元素4...};
int[] arr = new int[]{11,22,33};
double[] arr = new double[]{1.1,1.2,1.3};
// 数据类型[] 数组名 = {元素1，元素2，元素3，元素4...};
int[] array = {1,2,3,4,5};
double[] array = {1.1,1.2,1.3};
```

**动态初始化**，依然数组 **长度不可改变**，超过会越界异常，**初始化默认值**

```java
// 数据类型[] 数组名 = new 数据类型[数组的长度];
int[] scoresArr = new int[10];
```

### 遍历

数组地址值：数组名就是数组的首地址。

索引：数组索引从 0 开始，到 `len-1`。可以通过索引直接访问数组的值。

```java
int anOtherArray[] = new int[] {1, 2, 3, 4, 5};
for (int i = 0; i < anOtherArray.length; i++) {
    System.out.println(anOtherArray[i]);
}// 方法一
for (int element : anOtherArray) {
    System.out.println(element);
} // 方法二
```

## 字符串

```java
public final class String
    implements java.io.Serializable, Comparable<String>, CharSequence {
    private final char value[];
}
public final class String //9
    implements java.io.Serializable,Comparable<String>, CharSequence {
    private final byte[] value;
}

```

**Java 9 为何要将 `String` 的底层实现由 `char[]` 改成了 `byte[]` ?** 答：`byte` 占一个字节(8 位)，`char` 占用 2 个字节（16），`byte` 相较 `char` 节省一半的内存空间。

### 字符串不可变

保存字符串的数组被 `final` 修饰且为私有的，并且 `String` 类没有提供/暴露修改这个字符串的方法。

`String` 类被 `final` 修饰导致其不能被继承，进而避免了子类破坏 `String` 不可变。

### 字符串拼接

字符串对象通过 `+` 的字符串拼接方式，实际上是通过 `StringBuilder` 调用 `append()` 方法实现的，拼接完成之后调用 `toString()` 得到一个 `String` 对象 。

在 **循环** 内使用 `+` 进行字符串的拼接的话，存在比较明显的缺陷：**编译器不会创建单个 `StringBuilder` 以复用，会导致创建过多的 `StringBuilder` 对象**。但如果直接创建 `StringBuilder`，在循环里直接使用 `append()` 就不会产生这个问题。

### 字符串比较

`==`：比较地址

`equals`：比较内容。我们知道所有类的顶级父类是 `Object`，在 `Object` 的 `equals` 方法是比较的对象的 **内存地址**。`String` 中的 `equals` 方法是被 **重写** 过的，比较的是 String 字符串的 **值是否相等**。

`compareTo`：比较大小

`.contentEquals()`：字符串与任何的字符序列（StringBuffer、StringBuilder、String、CharSequence）进行比较。

### 其他

详情请看，**2.5.6 字符串常量池**

## 方法

```java
 修饰符 返回值类型 方法名(参数) {
   方法体; 
   return 数据;
}
```

### 实例方法

**没有使用 static 关键字修饰**，但在 **类中声明的方法** 被称为实例方法，在 **调用实例方法之前，必须创建类的对象**。

这代表实力方法是 **属于类的实例**。

```java
public class Add {
    public int a;
    
    public int add(int b) { // 实例方法
        return this.a + b;
    }
}
```

### 静态方法

被 **static 关键字修饰** 的方法就叫做静态方法。

```java
public class StaticMethodExample {
    public static void main(String[] args) {
        System.out.println(add(1,2));
    }
    public static int add(int a, int b) {
        return a + b;
    }
}
```

mian 和 add 方法都是静态方法，不同的是，**main 方法是程序的入口**。

静态方法是 **属于类** 的，不属于类实例的。**不需要通过 new 关键字创建对象来调用，直接通过类名就可以调用**。

### 抽象方法

**没有方法体的方法被称为抽象方法**，它总是在 **抽象类中声明**。这意味着如果类有抽象方法的话，这个类就必须是抽象的。可以使用 **abstract 关键字创建抽象方法和抽象类**。**当一个类继承了抽象类后，就必须重写抽象方法**。

```java
abstract class AbstractDemo {
    abstract void display();
}
public class MyAbstractDemo extends AbstractDemo {
    @Override
    void display() {
        System.out.println("重写了抽象方法");
    }
    public static void main(String[] args) {
        MyAbstractDemo myAbstractDemo = new MyAbstractDemo();
        myAbstractDemo.display();
    }
}
```

## Object

是所有类的父类，而且 Object 没有父类。其主要提供一下方法：

```java
/**
 * native 方法，用于返回当前运行时对象的 Class 对象，使用了 final 关键字修饰，故不允许子类重写。
 */
public final native Class<?> getClass()
/**
 * native 方法，用于返回对象的哈希码，主要使用在哈希表中，比如 JDK 中的HashMap。
 */
public native int hashCode()
/**
 * 用于比较 2 个对象的内存地址是否相等，String 类对该方法进行了重写以用于比较字符串的值是否相等。
 */
public boolean equals(Object obj)
/**
 * native 方法，用于创建并返回当前对象的一份拷贝。
 */
protected native Object clone() throws CloneNotSupportedException
/**
 * 返回类的名字实例的哈希码的 16 进制的字符串。建议 Object 所有的子类都重写这个方法。
 */
public String toString()
/**
 * native 方法，并且不能重写。唤醒一个在此对象监视器上等待的线程(监视器相当于就是锁的概念)。如果有多个线程在等待只会任意唤醒一个。
 */
public final native void notify()
/**
 * native 方法，并且不能重写。跟 notify 一样，唯一的区别就是会唤醒在此对象监视器上等待的所有线程，而不是一个线程。
 */
public final native void notifyAll()
/**
 * native方法，并且不能重写。暂停线程的执行。注意：sleep 方法没有释放锁，而 wait 方法释放了锁 ，timeout 是等待时间。
 */
public final native void wait(long timeout) throws InterruptedException
/**
 * 多了 nanos 参数，这个参数表示额外时间（以纳秒为单位，范围是 0-999999）。 所以超时的时间还需要加上 nanos 纳秒。。
 */
public final void wait(long timeout, int nanos) throws InterruptedException
/**
 * 跟之前的2个wait方法一样，只不过该方法一直等待，没有超时时间这个概念
 */
public final void wait() throws InterruptedException
/**
 * 实例被垃圾回收器回收的时候触发的操作
 */
protected void finalize() throws Throwable { }
```

`equals()` 方法存在两种使用情况：

- **类没有重写 `equals()` 方法**：通过 `equals()` 比较该类的两个对象时，等价于通过 `==` 比较这两个对象，使用的默认是 `Object` 类 `equals()` 方法。即比较地址
- **类重写了 `equals()` 方法**：一般我们都重写 `equals()` 方法来比较两个对象中的属性是否相等；若它们的属性相等，则返回 true(即，认为这两个对象相等)。比如，在 `String` 中，比较字符串的值。

`hashCode()` 的作用是获取哈希码（`int` 整数），也称为散列码。

- 如果两个对象的 `hashCode` 值相等，那这两个对象不一定相等（哈希碰撞）。
- 如果两个对象的 `hashCode` 值相等并且 `equals()` 方法也返回 `true`，我们才认为这两个对象相等。
- 如果两个对象的 `hashCode` 值不相等，我们就可以直接认为这两个对象不相等。

问题：**为什么重写 equals() 时必须重写 hashCode() 方法？** 答：因为两个相等的对象的 `hashCode` 值必须是相等。也就是说如果 `equals` 方法判断两个对象是相等的，那这两个对象的 `hashCode` 值也要相等。如果重写 `equals()` 时没有重写 `hashCode()` 方法的话就可能会导致 `equals` 方法判断是相等的两个对象，`hashCode` 值却不相等。

## 面向对象

### 类

类中包含三部分，成员变量、成员方法、构造方法。

类属于是对象，因此使用类需要进行实例，通过 `new` 进行声明。并且通过实例访问成员变量与成员方法

```java
public class Phone {
    //成员变量
    public String brand;
    public int price;
    //成员方法
    public void call() {
        System.out.println(&quot;打电话&quot;);
    }
    public void sendMessage() {
        System.out.println(&quot;发短信&quot;);
    }
}
public class PhoneDemo {
    public static void main(String[] args) {
        //创建对象
        Phone p = new Phone();
        //使用成员变量
        System.out.println(p.brand);
        System.out.println(p.price);
        //使用成员方法
        p.call();
        p.sendMessage();
    }
}
```

成员变量初始化

```java
// 方法一 在类内部直接初始化
public class Phone {
    public String brand = "HuaWei";
}
// 方法二 外部赋值 不建议 
// 方法三 set方法 通过成员方法赋值
public class Phone {
    String brand;
    public void setBrand(String brand){
        this.brand = brand;
    }
}
// 方法四 构造函数
```

### 构造方法

构造方法是特殊的成员方法，其符合下面规则：

- 构造方法的名字必须和类名一样；
- 构造方法没有返回类型，包括 void；如果写了 void，则会被认为是成员方法
- 构造方法不能是抽象的（abstract）、静态的（static）、最终的（final）、同步的（synchronized）。

```java
public class Person {
    private String name;
    private int age;
    private int sex;
    public Person() {
        this.name = "qyb";
        this.age = 18;
    }
    public Person(String name, int age, int sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
    }
    public Person(String name, int age) { // 构造重载
        this.name = name;
        this.age = age;
    }
}
```

分为 **无参构造** 与 **有参构造**。如果没有有参构造，Java 会自动生成无参构造，写不写无所谓。但写了有参构造，不会生成无参构造，需要手动写。

### 代码块

```java
public class Example {
    // 静态变量
    public static int staticVar = 1;
    // 实例变量
    public int instanceVar = 2;
    // 静态初始化块
    static {
        System.out.println("执行静态初始化块");
        staticVar = 3;
    }
    // 实例初始化块
    {
        System.out.println("执行实例初始化块");
        instanceVar = 4;
    }
    // 构造方法
    public Example() {
        System.out.println("执行构造方法");
    }
    public static void main(String[] args) {
        System.out.println("执行main方法");
        Example e1 = new Example();
        System.out.println("e1的静态变量：" + e1.staticVar);
        System.out.println("e1的实例变量：" + e1.instanceVar);
    }
}
/*执行静态初始化块
执行main方法
执行实例初始化块
执行构造方法
e1的静态变量：3
e1的实例变量：4*/
```

通过上面代码，可以知道程序运行的先后顺序：在 **加载类** 的时候 **首先运行静态代码块**，之后执行 `main` 方法，之后进行 **实例化对象** 时，先运 **行代码块**，之后构造函数。

### 继承

```java
public class Human {
  private String name ;
  private int age ;
  public String getName() {return name;}
  public void setName(String name) {this.name = name;}
  public int getAge() {return age;}
  public void setAge(int age) {this.age = age;}
}
public class Teacher extends Human {
  private double salary ;
  public void teach(){System.out.println("老师在认真教技术！");}
  public double getSalary() {return salary;}
  public void setSalary(double salary) {this.salary = salary;}
}
```

继承实际上是子类相同的属性和行为可以定义在父类中，子类特有的属性和行为由自己定义，这样就实现了相同属性和行为的重复利用，从而提高了代码复用。Java 语言只支持类的 **单继承**，但可以 **多层继承**。

#### 继承的特点

1. **不能继承父类的构造方法**。但注意，**子类构建时必须使用父类的构造方法**。所以子类构造方法必须调用 `super()` 即父类的构造方法。

   ```java
   class A{
       public String name;
       public A() {//无参构造
       }
       public A (String name){//有参构造
       }
   }
   class B extends A{
       public int age;
       public B() {//无参构造
           super();
       }
       public B(String name,int age) {//有参构造
           super(name);
           this.age = age;
       }
   }
   // super关键字，表示父类
   // this关键字，表示当前类
   ```

2. 子类 **可以继承父类的私有成员**（成员变量，方法），只是子类 **无法直接访问** 而已，可以通过 **方法访问父类的私有成员变量**。

3. 如果子类父类中出现 **重名** 的成员变量，这时的访问是 **有影响的**。子父类中出现了同名的成员变量时，子类会优先访问自己对象中的成员变量。我们可以使用 `super` 关键字，表示使用的父类的成员变量。

4. 子类中出现与父类一模一样的方法时（**返回值类型，方法名和参数列表都相同**），会出现 **覆盖** 效果，也称为重写。

   ```java
   public class Cat extends Animal {
       @Override // @Override注解证明这个方法是重写的
       public void cry(){
           System.out.println('我们一起学猫叫，喵喵喵！喵的非常好听');
       }
   }
   ```

5. 向上转型：通过子类对象(小范围)实例化父类对象(大范围)。`Father f = new Son()`

6. 向下转型：通过父类对象强制转化为子类对象。`Son son = (Son) f;`

7. 继承中初始化顺序：父类中静态成员变量和静态代码块 >>> 子类中静态成员变量和静态代码块 >>> 父类中普通成员变量和代码块，父类的构造方法 >>> 子类中普通成员变量和代码块，子类的构造方法。

### 多态

Java 的多态的前提

- 子类继承父类
- 子类重写父类的方法
- 父类引用指向子类的对象

调用成员变量时：编译看左边，运行看左边

调用成员方法时：编译看左边，运行看右边

```java
Fu f = new Zi(); // 多态依赖于向上转型
//编译看左边的父类中有没有name这个属性，没有就报错
//在实际运行的时候，把父类name属性的值打印出来
System.out.println(f.name);
//编译看左边的父类中有没有show这个方法，没有就报错
//在实际运行的时候，运行的是子类中的show方法
f.show();
```

### 抽象类

```java
public abstract class Player { // 被abstract修饰的类叫抽象类
    public abstract void play(); // 抽象方法: 无方法体的方法，被abstract修饰。
    // 只能在抽象类中定义，必须在子类中实现
    
    public void sleep() { // 抽象类中不一定都是抽象方法
        System.out.println("sleep");
    }
}
public class BasketballPlayer extends AbstractPlayer { // 抽象类无法实例化，只能通过继承实现
    @Override
    void play() {
        System.out.println("我是张伯伦，篮球场上得过 100 分");
    }
}
```

1. 抽象类不能创建对象，只能创建其非抽象子类的对象。
2. 抽象类中，**可以有构造方法**，是供子类创建对象时，初始化父类成员使用的。
3. 抽象类中，**不一定包含抽象方法，但是有抽象方法的类必定是抽象类**。
4. 抽象类的子类，必须重写抽象父类中 **所有的** 抽象方法，否则子类也必须定义成抽象类，编译无法通过而报错。
5. 抽象类存在的意义是为了被子类继承。

### 接口

```java
//不要在定义接口的时候使用 final 关键字
public interface InterFace01 { // 被interface修饰的类
    // 只有包含：抽象方法和常量，静态方法，默认方法
    public int context1 = 45; //在接口中定义的成员变量默认会加上public static final
    public void contextTest(); //在接口中的抽象方法默认会自动加上public abstract
    //接口的抽象方法不能是 private、protected 或者 final
    // JDK8之后，接口中可以写静态方法
    static boolean isEnergyEfficient(String electtronicType) {
        return electtronicType.equals(LED);
    }

    // 默认方法
    default void printDescription() {
        System.out.println("电子");
    }
}
public class interface01_test1 implements InterFace01{ // 接口同样不能实例化，通过implements实现
    @Override
    public void contextTest(){ // 实现自己的方法
        System.out.println("everyThing is ok 1");
    }
}
// 接口可与被继承
public interface InterFace01 extends InterFace01 {
    void read();
}
```

### 内部类

1. 成员内部类

   ```java
   public class Wanger {
       int age = 18;
       private String name = "沉默王二";
       static double money = 1;
       public Wanger () {
           new Wangxiaoer().print(); // 但外部调用内部类的变量或方法需要先实例化
       }
       class Wangxiaoer { // 这就是一个内部类，在一个类中，再写一个类
           int age = 81;
   
           public void print() {//内部可以随意调用外部的成员变量与方法
               System.out.println(name);
               System.out.println(money);
           }
       }
   }
   ```

2. 静态内部类

   ```java
   public class Wangsi {
       static int age;
       double money;
       
       static class Wangxxiaosi { //成员内部类被static修饰
           public Wangxxiaosi (){
               System.out.println(age);// 静态内部类是不允许访问外部类中非 static 的变量和方法的
           }
       }
   }
   ```

3. 局部内部类

   ```java
   public class Wangsan {
       public Wangsan print() {
           class Wangxiaosan extends Wangsan{// 在方法或一个作用域中声明的类，是一个局部的，随方法/作用域的消亡而消亡
               private int age = 18;
           }
           return new Wangxiaosan();
       }
   }
   ```

   

4. 匿名内部类

   ```java
   public class ThreadDemo {
       public static void main(String[] args) {
           Thread t = new Thread(
               new Runnable() {// 一个匿名内部类
                   @Override
                   public void run() {
                       System.out.println(Thread.currentThread().getName());
                   }
               });
           t.start();
       }
   }
   ```

   匿名顾名思义，没有名字，直接实例化使用。**匿名内部类是唯一一种没有构造方法的类**。它直接通过 new 关键字创建出来的一个对象。

## 集合



## 时间类

### Date

`java.util.Date` 类 表示特定的瞬间，精确到毫秒。

继续查阅 Date 类的描述，发现 Date 拥有多个构造函数，只是部分已经过时，我们重点看以下两个构造函数

- `public Date()`：从运行程序的此时此刻到时间原点经历的毫秒值, 转换成 Date 对象，分配 Date 对象并初始化此对象，以表示分配它的时间（精确到毫秒）。
- `public Date(long date)`：将指定参数的毫秒值 date, 转换成 Date 对象，分配 Date 对象并初始化此对象，以表示自从标准基准时间

简单来说：使用无参构造，可以自动设置当前系统时间的毫秒时刻；指定 long 类型的构造参数，可以自定义毫秒时刻。例如：

```java
import java.util.Date;

public class Demo01Date {
    public static void main(String[] args) {
        // 创建日期对象，把当前的时间
        System.out.println(new Date()); // Tue Jan 16 14:37:35 CST 2020
        // 创建日期对象，把当前的毫秒值转成日期对象
        System.out.println(new Date(0L)); // Thu Jan 01 08:00:00 CST 1970
    }
}
```

####  Date 常用方法

Date 类常用的方法有：

- `public long getTime()` 把日期对象转换成对应的时间毫秒值。
- `public void setTime(long time)` 把方法参数给定的毫秒值设置给日期对象

示例代码

```java
public class DateDemo02 {
    public static void main(String[] args) {
        //创建日期对象
        Date d = new Date();
        
        //public long getTime():获取的是日期对象从1970年1月1日 00:00:00到现在的毫秒值
        //System.out.println(d.getTime());
        //System.out.println(d.getTime() * 1.0 / 1000 / 60 / 60 / 24 / 365 + &quot;年&quot;);

        //public void setTime(long time):设置时间，给的是毫秒值
        //long time = 1000*60*60;
        long time = System.currentTimeMillis();
        d.setTime(time);

        System.out.println(d);
    }
}
```

### SimpleDateFormat 类

`java.text.SimpleDateFormat` 是日期/时间格式化类，我们通过这个类可以帮我们完成日期和文本之间的转换, 也就是可以在 Date 对象与 String 对象之间进行来回转换。

- **格式化**：按照指定的格式，把 Date 对象转换为 String 对象。
- **解析**：按照指定的格式，把 String 对象转换为 Date 对象。

#### 构造方法

由于 DateFormat 为抽象类，不能直接使用，所以需要常用的子类 `java.text.SimpleDateFormat`。这个类需要一个模式（格式）来指定格式化或解析的标准。构造方法为：

- `public SimpleDateFormat(String pattern)`：用给定的模式和默认语言环境的日期格式符号构造 SimpleDateFormat。参数 pattern 是一个字符串，代表日期时间的自定义格式。

#### 格式规则

常用的格式规则为：

| 标识字母（区分大小写） | 含义 |
| ---------------------- | ---- |
| y                      | 年   |
| M                      | 月   |
| d                      | 日   |
| H                      | 时   |
| m                      | 分   |
| s                      | 秒   |

#### 常用方法

DateFormat 类的常用方法有：

- `public String format(Date date)`：将 Date 对象格式化为字符串。

- `public Date parse(String source)`：将字符串解析为 Date 对象。

  ```java
  package com.itheima.a01jdk7datedemo;
  
  import java.text.ParseException;
  import java.text.SimpleDateFormat;
  import java.util.Date;
  
  public class A03_SimpleDateFormatDemo1 {
      public static void main(String[] args) throws ParseException {
          /*
              public simpleDateFormat() 默认格式
              public simpleDateFormat(String pattern) 指定格式
              public final string format(Date date) 格式化(日期对象 -&gt;字符串)
              public Date parse(string source) 解析(字符串 -&gt;日期对象)
          */
  
          //1.定义一个字符串表示时间
          String str = &quot;2023-11-11 11:11:11&quot;;
          //2.利用空参构造创建simpleDateFormat对象
          // 细节:
          //创建对象的格式要跟字符串的格式完全一致
          SimpleDateFormat sdf = new SimpleDateFormat(&quot;yyyy-MM-dd HH:mm:ss&quot;);
          Date date = sdf.parse(str);
          //3.打印结果
          System.out.println(date.getTime());//1699672271000
  
  
      }
  
      private static void method1() {
          //1.利用空参构造创建simpleDateFormat对象，默认格式
          SimpleDateFormat sdf1 = new SimpleDateFormat();
          Date d1 = new Date(0L);
          String str1 = sdf1.format(d1);
          System.out.println(str1);//1970/1/1 上午8:00
  
          //2.利用带参构造创建simpleDateFormat对象，指定格式
          SimpleDateFormat sdf2 = new SimpleDateFormat(&quot;yyyy年MM月dd日HH:mm:ss&quot;);
          String str2 = sdf2.format(d1);
          System.out.println(str2);//1970年01月01日 08:00:00
  
          //课堂练习:yyyy年MM月dd日 时:分:秒 星期
      }
  }
  ```

### Calendar 类

- java.util.Calendar 类表示一个“日历类”，可以进行日期运算。它是一个抽象类，不能创建对象，我们可以使用它的子类：java.util.GregorianCalendar 类。
- 有两种方式可以获取 GregorianCalendar 对象：
  - 直接创建 GregorianCalendar 对象；
  - 通过 Calendar 的静态方法 getInstance()方法获取 GregorianCalendar 对象【本次课使用】

#### 常用方法

| 方法名                                | 说明                                                         |
| ------------------------------------- | ------------------------------------------------------------ |
| public static Calendar getInstance()  | 获取一个它的子类 GregorianCalendar 对象。                      |
| public int get(int field)             | 获取某个字段的值。field 参数表示获取哪个字段的值，<br /> 可以使用 Calender 中定义的常量来表示：<br /> Calendar.YEAR : 年 <br /> Calendar.MONTH ：月 <br /> Calendar.DAY_OF_MONTH：月中的日期 <br /> Calendar.HOUR：小时 <br /> Calendar.MINUTE：分钟 <br /> Calendar.SECOND：秒 <br /> Calendar.DAY_OF_WEEK：星期 |
| public void set(int field, int value)  | 设置某个字段的值                                             |
| public void add(int field, int amount) | 为某个字段增加/减少指定的值                                  |

```java
public class Demo {
    public static void main(String[] args) {
        //1.获取一个GregorianCalendar对象
        Calendar instance = Calendar.getInstance();//获取子类对象

        //2.打印子类对象
        System.out.println(instance);

        //3.获取属性
        int year = instance.get(Calendar.YEAR);
        int month = instance.get(Calendar.MONTH) + 1;//Calendar的月份值是0-11
        int day = instance.get(Calendar.DAY_OF_MONTH);

        int hour = instance.get(Calendar.HOUR);
        int minute = instance.get(Calendar.MINUTE);
        int second = instance.get(Calendar.SECOND);

        int week = instance.get(Calendar.DAY_OF_WEEK);//返回值范围：1--7，分别表示：&quot;星期日&quot;,&quot;星期一&quot;,&quot;星期二&quot;,...,&quot;星期六&quot;

        System.out.println(year + &quot;年&quot; + month + &quot;月&quot; + day + &quot;日&quot; + 
                           	hour + &quot;:&quot; + minute + &quot;:&quot; + second);
        System.out.println(getWeek(week));

    }

    //查表法，查询星期几
    public static String getWeek(int w) {//w = 1 --- 7
        //做一个表(数组)
        String[] weekArray = {&quot;星期日&quot;, &quot;星期一&quot;, &quot;星期二&quot;, &quot;星期三&quot;, &quot;星期四&quot;, &quot;星期五&quot;, &quot;星期六&quot;};
        //            索引      [0]      [1]       [2]      [3]       [4]      [5]      [6]
        //查表
        return weekArray[w - 1];
    }
}
```

```java
public class Demo {
    public static void main(String[] args) {
        //设置属性——set(int field,int value):
		Calendar c1 = Calendar.getInstance();//获取当前日期

		//计算班长出生那天是星期几(假如班长出生日期为：1998年3月18日)
		c1.set(Calendar.YEAR, 1998);
		c1.set(Calendar.MONTH, 3 - 1);//转换为Calendar内部的月份值
		c1.set(Calendar.DAY_OF_MONTH, 18);

		int w = c1.get(Calendar.DAY_OF_WEEK);
		System.out.println(&quot;班长出生那天是：&quot; + getWeek(w));

        
    }
    //查表法，查询星期几
    public static String getWeek(int w) {//w = 1 --- 7
        //做一个表(数组)
        String[] weekArray = {&quot;星期日&quot;, &quot;星期一&quot;, &quot;星期二&quot;, &quot;星期三&quot;, &quot;星期四&quot;, &quot;星期五&quot;, &quot;星期六&quot;};
        //            索引      [0]      [1]       [2]      [3]       [4]      [5]      [6]
        //查表
        return weekArray[w - 1];
    }
}
```

```java
public class Demo {
    public static void main(String[] args) {
        //计算200天以后是哪年哪月哪日，星期几？
		Calendar c2 = Calendar.getInstance();//获取当前日期
        c2.add(Calendar.DAY_OF_MONTH, 200);//日期加200

        int y = c2.get(Calendar.YEAR);
        int m = c2.get(Calendar.MONTH) + 1;//转换为实际的月份
        int d = c2.get(Calendar.DAY_OF_MONTH);

        int wk = c2.get(Calendar.DAY_OF_WEEK);
        System.out.println(&quot;200天后是：&quot; + y + &quot;年&quot; + m + &quot;月&quot; + d + &quot;日&quot; + getWeek(wk));

    }
    //查表法，查询星期几
    public static String getWeek(int w) {//w = 1 --- 7
        //做一个表(数组)
        String[] weekArray = {&quot;星期日&quot;, &quot;星期一&quot;, &quot;星期二&quot;, &quot;星期三&quot;, &quot;星期四&quot;, &quot;星期五&quot;, &quot;星期六&quot;};
        //            索引      [0]      [1]       [2]      [3]       [4]      [5]      [6]
        //查表
        return weekArray[w - 1];
    }
}
```

### JDK8 时间相关类

| JDK8 时间类类名    | 作用                   |
| ----------------- | ---------------------- |
| ZoneId            | 时区                   |
| Instant           | 时间戳                 |
| ZoneDateTime      | 带时区的时间           |
| DateTimeFormatter | 用于时间的格式化和解析 |
| LocalDate         | 年、月、日             |
| LocalTime         | 时、分、秒             |
| LocalDateTime     | 年、月、日、时、分、秒 |
| Duration          | 时间间隔（秒，纳，秒） |
| Period            | 时间间隔（年，月，日） |
| ChronoUnit        | 时间间隔（所有单位）   |

```java
/*
        static Set&lt;string&gt; getAvailableZoneIds() 获取Java中支持的所有时区
        static ZoneId systemDefault() 获取系统默认时区
        static Zoneld of(string zoneld) 获取一个指定时区
        */

//1.获取所有的时区名称
Set&lt;String&gt; zoneIds = ZoneId.getAvailableZoneIds();
System.out.println(zoneIds.size());//600
System.out.println(zoneIds);// Asia/Shanghai

//2.获取当前系统的默认时区
ZoneId zoneId = ZoneId.systemDefault();
System.out.println(zoneId);//Asia/Shanghai

//3.获取指定的时区
ZoneId zoneId1 = ZoneId.of(&quot;Asia/Pontianak&quot;);
System.out.println(zoneId1);//Asia/Pontianak
```

```java
/*
            static Instant now() 获取当前时间的Instant对象(标准时间)
            static Instant ofXxxx(long epochMilli) 根据(秒/毫秒/纳秒)获取Instant对象
            ZonedDateTime atZone(ZoneIdzone) 指定时区
            boolean isxxx(Instant otherInstant) 判断系列的方法
            Instant minusXxx(long millisToSubtract) 减少时间系列的方法
            Instant plusXxx(long millisToSubtract) 增加时间系列的方法
        */
//1.获取当前时间的Instant对象(标准时间)
Instant now = Instant.now();
System.out.println(now);

//2.根据(秒/毫秒/纳秒)获取Instant对象
Instant instant1 = Instant.ofEpochMilli(0L);
System.out.println(instant1);//1970-01-01T00:00:00z

Instant instant2 = Instant.ofEpochSecond(1L);
System.out.println(instant2);//1970-01-01T00:00:01Z

Instant instant3 = Instant.ofEpochSecond(1L, 1000000000L);
System.out.println(instant3);//1970-01-01T00:00:027

//3. 指定时区
ZonedDateTime time = Instant.now().atZone(ZoneId.of(&quot;Asia/Shanghai&quot;));
System.out.println(time);


//4.isXxx 判断
Instant instant4=Instant.ofEpochMilli(0L);
Instant instant5 =Instant.ofEpochMilli(1000L);

//5.用于时间的判断
//isBefore:判断调用者代表的时间是否在参数表示时间的前面
boolean result1=instant4.isBefore(instant5);
System.out.println(result1);//true

//isAfter:判断调用者代表的时间是否在参数表示时间的后面
boolean result2 = instant4.isAfter(instant5);
System.out.println(result2);//false

//6.Instant minusXxx(long millisToSubtract) 减少时间系列的方法
Instant instant6 =Instant.ofEpochMilli(3000L);
System.out.println(instant6);//1970-01-01T00:00:03Z

Instant instant7 =instant6.minusSeconds(1);
System.out.println(instant7);//1970-01-01T00:00:02Z
```

```java
/*
            static ZonedDateTime now() 获取当前时间的ZonedDateTime对象
            static ZonedDateTime ofXxxx(。。。) 获取指定时间的ZonedDateTime对象
            ZonedDateTime withXxx(时间) 修改时间系列的方法
            ZonedDateTime minusXxx(时间) 减少时间系列的方法
            ZonedDateTime plusXxx(时间) 增加时间系列的方法
         */
//1.获取当前时间对象(带时区)
ZonedDateTime now = ZonedDateTime.now();
System.out.println(now);

//2.获取指定的时间对象(带时区)1/年月日时分秒纳秒方式指定
ZonedDateTime time1 = ZonedDateTime.of(2023, 10, 1,
                                       11, 12, 12, 0, ZoneId.of(&quot;Asia/Shanghai&quot;));
System.out.println(time1);

//通过Instant + 时区的方式指定获取时间对象
Instant instant = Instant.ofEpochMilli(0L);
ZoneId zoneId = ZoneId.of(&quot;Asia/Shanghai&quot;);
ZonedDateTime time2 = ZonedDateTime.ofInstant(instant, zoneId);
System.out.println(time2);


//3.withXxx 修改时间系列的方法
ZonedDateTime time3 = time2.withYear(2000);
System.out.println(time3);

//4. 减少时间
ZonedDateTime time4 = time3.minusYears(1);
System.out.println(time4);

//5.增加时间
ZonedDateTime time5 = time4.plusYears(1);
System.out.println(time5);
```

```java
/*
            static DateTimeFormatter ofPattern(格式) 获取格式对象
            String format(时间对象) 按照指定方式格式化
        */
//获取时间对象
ZonedDateTime time = Instant.now().atZone(ZoneId.of(&quot;Asia/Shanghai&quot;));

// 解析/格式化器
DateTimeFormatter dtf1=DateTimeFormatter.ofPattern(&quot;yyyy-MM-dd HH:mm;ss EE a&quot;);
// 格式化
System.out.println(dtf1.format(time));
```

```java
//1.获取当前时间的日历对象(包含 年月日)
LocalDate nowDate = LocalDate.now();
//System.out.println(&quot;今天的日期:&quot; + nowDate);
//2.获取指定的时间的日历对象
LocalDate ldDate = LocalDate.of(2023, 1, 1);
System.out.println(&quot;指定日期:&quot; + ldDate);

System.out.println(&quot;=============================&quot;);

//3.get系列方法获取日历中的每一个属性值//获取年
int year = ldDate.getYear();
System.out.println(&quot;year: &quot; + year);
//获取月//方式一:
Month m = ldDate.getMonth();
System.out.println(m);
System.out.println(m.getValue());

//方式二:
int month = ldDate.getMonthValue();
System.out.println(&quot;month: &quot; + month);


//获取日
int day = ldDate.getDayOfMonth();
System.out.println(&quot;day:&quot; + day);

//获取一年的第几天
int dayofYear = ldDate.getDayOfYear();
System.out.println(&quot;dayOfYear:&quot; + dayofYear);

//获取星期
DayOfWeek dayOfWeek = ldDate.getDayOfWeek();
System.out.println(dayOfWeek);
System.out.println(dayOfWeek.getValue());

//is开头的方法表示判断
System.out.println(ldDate.isBefore(ldDate));
System.out.println(ldDate.isAfter(ldDate));

//with开头的方法表示修改，只能修改年月日
LocalDate withLocalDate = ldDate.withYear(2000);
System.out.println(withLocalDate);

//minus开头的方法表示减少，只能减少年月日
LocalDate minusLocalDate = ldDate.minusYears(1);
System.out.println(minusLocalDate);


//plus开头的方法表示增加，只能增加年月日
LocalDate plusLocalDate = ldDate.plusDays(1);
System.out.println(plusLocalDate);

//-------------
// 判断今天是否是你的生日
LocalDate birDate = LocalDate.of(2000, 1, 1);
LocalDate nowDate1 = LocalDate.now();

MonthDay birMd = MonthDay.of(birDate.getMonthValue(), birDate.getDayOfMonth());
MonthDay nowMd = MonthDay.from(nowDate1);

System.out.println(&quot;今天是你的生日吗? &quot; + birMd.equals(nowMd));//今天是你的生日吗?
```

```java
// 获取本地时间的日历对象。(包含 时分秒)
LocalTime nowTime = LocalTime.now();
System.out.println(&quot;今天的时间:&quot; + nowTime);

int hour = nowTime.getHour();//时
System.out.println(&quot;hour: &quot; + hour);

int minute = nowTime.getMinute();//分
System.out.println(&quot;minute: &quot; + minute);

int second = nowTime.getSecond();//秒
System.out.println(&quot;second:&quot; + second);

int nano = nowTime.getNano();//纳秒
System.out.println(&quot;nano:&quot; + nano);
System.out.println(&quot;------------------------------------&quot;);
System.out.println(LocalTime.of(8, 20));//时分
System.out.println(LocalTime.of(8, 20, 30));//时分秒
System.out.println(LocalTime.of(8, 20, 30, 150));//时分秒纳秒
LocalTime mTime = LocalTime.of(8, 20, 30, 150);

//is系列的方法
System.out.println(nowTime.isBefore(mTime));
System.out.println(nowTime.isAfter(mTime));

//with系列的方法，只能修改时、分、秒
System.out.println(nowTime.withHour(10));

//plus系列的方法，只能修改时、分、秒
System.out.println(nowTime.plusHours(10));
```

```java
// 当前时间的的日历对象(包含年月日时分秒)
LocalDateTime nowDateTime = LocalDateTime.now();

System.out.println(&quot;今天是:&quot; + nowDateTime);//今天是：
System.out.println(nowDateTime.getYear());//年
System.out.println(nowDateTime.getMonthValue());//月
System.out.println(nowDateTime.getDayOfMonth());//日
System.out.println(nowDateTime.getHour());//时
System.out.println(nowDateTime.getMinute());//分
System.out.println(nowDateTime.getSecond());//秒
System.out.println(nowDateTime.getNano());//纳秒
// 日:当年的第几天
System.out.println(&quot;dayofYear:&quot; + nowDateTime.getDayOfYear());
//星期
System.out.println(nowDateTime.getDayOfWeek());
System.out.println(nowDateTime.getDayOfWeek().getValue());
//月份
System.out.println(nowDateTime.getMonth());
System.out.println(nowDateTime.getMonth().getValue());

LocalDate ld = nowDateTime.toLocalDate();
System.out.println(ld);

LocalTime lt = nowDateTime.toLocalTime();
System.out.println(lt.getHour());
System.out.println(lt.getMinute());
System.out.println(lt.getSecond());
```

```java
// 本地日期时间对象。
LocalDateTime today = LocalDateTime.now();
System.out.println(today);

// 出生的日期时间对象
LocalDateTime birthDate = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
System.out.println(birthDate);

Duration duration = Duration.between(birthDate, today);//第二个参数减第一个参数
System.out.println(&quot;相差的时间间隔对象:&quot; + duration);

System.out.println(&quot;============================================&quot;);
System.out.println(duration.toDays());//两个时间差的天数
System.out.println(duration.toHours());//两个时间差的小时数
System.out.println(duration.toMinutes());//两个时间差的分钟数
System.out.println(duration.toMillis());//两个时间差的毫秒数
System.out.println(duration.toNanos());//两个时间差的纳秒数
```

```java
// 当前本地 年月日
LocalDate today = LocalDate.now();
System.out.println(today);

// 生日的 年月日
LocalDate birthDate = LocalDate.of(2000, 1, 1);
System.out.println(birthDate);

Period period = Period.between(birthDate, today);//第二个参数减第一个参数

System.out.println(&quot;相差的时间间隔对象:&quot; + period);
System.out.println(period.getYears());
System.out.println(period.getMonths());
System.out.println(period.getDays());

System.out.println(period.toTotalMonths());
```

```java
// 当前时间
LocalDateTime today = LocalDateTime.now();
System.out.println(today);
// 生日时间
LocalDateTime birthDate = LocalDateTime.of(2000, 1, 1,0, 0, 0);
System.out.println(birthDate);

System.out.println(&quot;相差的年数:&quot; + ChronoUnit.YEARS.between(birthDate, today));
System.out.println(&quot;相差的月数:&quot; + ChronoUnit.MONTHS.between(birthDate, today));
System.out.println(&quot;相差的周数:&quot; + ChronoUnit.WEEKS.between(birthDate, today));
System.out.println(&quot;相差的天数:&quot; + ChronoUnit.DAYS.between(birthDate, today));
System.out.println(&quot;相差的时数:&quot; + ChronoUnit.HOURS.between(birthDate, today));
System.out.println(&quot;相差的分数:&quot; + ChronoUnit.MINUTES.between(birthDate, today));
System.out.println(&quot;相差的秒数:&quot; + ChronoUnit.SECONDS.between(birthDate, today));
System.out.println(&quot;相差的毫秒数:&quot; + ChronoUnit.MILLIS.between(birthDate, today));
System.out.println(&quot;相差的微秒数:&quot; + ChronoUnit.MICROS.between(birthDate, today));
System.out.println(&quot;相差的纳秒数:&quot; + ChronoUnit.NANOS.between(birthDate, today));
System.out.println(&quot;相差的半天数:&quot; + ChronoUnit.HALF_DAYS.between(birthDate, today));
System.out.println(&quot;相差的十年数:&quot; + ChronoUnit.DECADES.between(birthDate, today));
System.out.println(&quot;相差的世纪(百年)数:&quot; + ChronoUnit.CENTURIES.between(birthDate, today));
System.out.println(&quot;相差的千年数:&quot; + ChronoUnit.MILLENNIA.between(birthDate, today));
System.out.println(&quot;相差的纪元数:&quot; + ChronoUnit.ERAS.between(birthDate, today));
```

