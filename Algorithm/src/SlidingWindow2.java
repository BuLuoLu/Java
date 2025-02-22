import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// 双指针为不定长滑动窗口
// 重点维护窗口
// 一般都写双重循环 外层for扩大窗口 right++，统计变化
// 当不满足题目条件进入内循环while，缩小窗口left++ 统计变化
// 之后使统计信息 更新答案
// 如果使求最长/最短： ans = max/min(ans,right-left+1)
// 求数量是： 越长越好 ans+=left；越短越好ans+=right-left+1


public class SlidingWindow2 {
    /**
     * 不定长滑动窗口 一般解决最长/最短子数组(求一个)，与求子数组的个数问题(求多个)
     * 一般指双指针，双指针可以划分为同向，相向，异向
     * 按照序列个数可以分为单序列，多序列
     * 其双指针的关键点在于：
     * 维护窗口 以及 对应的满足条件的判断
     * 1同向、
     * 进入窗口时(left++),需不需要改变窗口的状态，如某个元素的个数，窗口內种类
     * 进入更新状态后，看是不是满足条件，不满足要缩小窗口(right++)
     * 同时要更改缩小后，单出来的这个值对状态的影响
     */

    // 最长子数组
    /** 力扣3
     * 给定一个字符串 s ，请你找出其中不含有重复字符的 最长子串的长度。
     * 单序列 + 同向双指针
     * 右指针直接进，之后判断有没有重复，如果有左指针退出减少窗口
     * 之后更新记录
     */
    public int lengthOfLongestSubstring(String S) {
        char[] s = S.toCharArray(); // 转换成 char[] 加快效率（忽略带来的空间消耗）
        int n = s.length;
        int ans = 0;
        int left = 0;
        int[] cnt = new int[128]; // 也可以用 HashMap<Character, Integer>，这里为了效率用的数组
        for (int right = 0; right < n; right++) {
            char c = s[right];
            cnt[c]++;
            while (cnt[c] > 1) { // 窗口内有重复字母
                cnt[s[left]]--; // 移除窗口左端点字母
                left++; // 缩小窗口
            }
            ans = Math.max(ans, right - left + 1); // 更新窗口长度最大值
        }
        return ans;
    }

    /** 力扣1493
     * 给你一个二进制数组 nums ，你需要从中删掉一个元素。
     * 请你在删掉元素的结果数组中，返回最长的且只包含 1 的非空子数组的长度。
     * 如果不存在这样的子数组，请返回 0 。
     */
    public static int longestSubarray(int[] nums) {
        int n = nums.length;
        int ans = 0;
        int flag = 0; // 用于维护窗口 窗口内0最多有一个
        for(int left=0, right=0; right<n; right++) {
            if(nums[right]==0) { flag++;} // 更新0的数量
            if(flag > 1) { //维护窗口 如果0的数量大于1 需要缩小窗口
                if(nums[left]==0) { flag--;} // 如果要缩小的是0，减少标志
                left++; // 缩小
            }
            ans = Math.max(ans, right - left);
        }
        return ans;
    }

    /** 力扣1208
     * 给你两个长度相同的字符串，s 和 t。
     * 将 s 中的第 i 个字符变到 t 中的第 i 个字符需要 |s[i] - t[i]| 的开销（开销可能为 0），也就是两个字符的 ASCII 码值的差的绝对值。
     * 用于变更字符串的最大预算是 maxCost。在转化字符串时，总开销应当小于等于该预算，这也意味着字符串的转化可能是不完全的。
     * 如果你可以将 s 的子字符串转化为它在 t 中对应的子字符串，则返回可以转化的最大长度。
     * 如果 s 中没有子字符串可以转化成 t 中对应的子字符串，则返回 0。
     */
    // 思路：要开销不能超过max，其实可以记录每一个词对应位置的开销，
    // 转化为一个单序列的滑动窗口
    public int equalSubstring(String s, String t, int maxCost) {
        char[] char_s = s.toCharArray();
        char[] char_t = t.toCharArray();
        int[] costs = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            costs[i] = Math.abs(char_s[i] - char_t[i]);
        }
        int sum = 0;
        int ans = 0;
        for(int right = 0, left=0; right < char_t.length; right++) {
            sum += costs[right];
            if(sum > maxCost) {
                sum -= costs[left];
                left++;
            }
            ans = Math.max(ans, right - left + 1);
        }
        return ans;
    }

    // 力扣904
    public int totalFruit(int[] fruits) {
        int lanzi = 0;
        int ans = 0;
        int[] fruit = new int[fruits.length];
        Arrays.fill(fruit, 0);
        for(int left = 0, right = 0; right < fruits.length; right++) {
            if(fruit[fruits[right]] == 0) {
                lanzi++;
            }
            fruit[fruits[right]]++;
            if(lanzi>2){
                fruit[fruits[left]]--;
                if(fruit[fruits[left]]==0){ lanzi--;}
                left++;
            }
            ans = Math.max(ans, right - left + 1);
        }
        return ans;
    }
//    public int totalFruit(int[] fruits) {
//            int ans = 0;
//            int left = 0;
//            Map<Integer, Integer> cnt = new HashMap<>();
//            for (int right = 0; right < fruits.length; right++) {
//                cnt.merge(fruits[right], 1, Integer::sum); // fruits[right] 进入窗口
//                while (cnt.size() > 2) { // 不满足要求
//                    int out = fruits[left];
//                    cnt.merge(out, -1, Integer::sum); // fruits[left] 离开窗口
//                    if (cnt.get(out) == 0) {
//                        cnt.remove(out);
//                    }
//                    left++;
//                }
//                ans = Math.max(ans, right - left + 1);
//            }
//            return ans;
//    }

    // 力扣1695
    // 遇上个题类似，如果需要记录数组中元素的个数或元素的种类，可以用map
    // 上一题中 窗口内只能有两种，这样map的长度不能超过二
    // 这一一种 要求每个元素不同 因此每个map的value不能超过1
    public int maximumUniqueSubarray(int[] nums) {
        Map<Integer, Integer> window = new HashMap<>();
        int left = 0, right = 0;
        int res = 0, cur = 0;
        while (right < nums.length) {
            int k = nums[right++];
            window.put(k, window.getOrDefault(k, 0) + 1);
            cur += k;
            while (window.get(k) > 1){
                int d = nums[left++];
                window.put(d, window.get(d) - 1);
                cur -= d;
            }
            res = Math.max(res, cur);
        }
        return res;
    }

    public int longestOnes(int[] nums, int k) {
        int ans = 0;
        int cnt0 = 0;
        for (int right = 0, left = 0; right < nums.length; right++) {
            if(nums[right]==0) {
                cnt0++;
            }
            while (cnt0 > k) {
                if(nums[left]==0){
                    cnt0--;
                }
                left++;
            }
            ans = Math.max(ans, right - left + 1);
        }
        return ans;
    }

    // 1658
    // 逆向思维 从nums中移除一个最长的子数组，使得剩余元素的和为x
    public int minOperations(int[] nums, int x) {
        int target = -x;
        for (int num : nums) {
            target += num;
        }
        if (target < 0) {
            return -1;
        }
        int ans = -1;
        int sum = 0;
        for (int right = 0, left = 0; right < nums.length; right++) {
            sum += nums[right];
            while (sum > target) {
                sum -= nums[left];
                left++;
            }
            if (sum == target) {
                ans = Math.max(ans, right - left + 1);
            }
        }
        return ans < 0 ? -1 : nums.length - ans;
    }

    // 209
    public static int minSubArrayLen(int target, int[] nums) {
            // 记录一个答案
            int ans = Integer.MAX_VALUE;
            // l,r 窗口左右边界，sum 窗口内的和
            for (int l = 0, r = 0, sum = 0; r < nums.length; r++) {
                // 一直向右移动窗口，直到窗口内的和大于等于target
                sum += nums[r];
                while (sum - nums[l] >= target) {
                    // 已经大于等于target了，看能不能缩小窗口l++
                    // sum : nums[l....r]
                    // 如果l位置的数从窗口出去，还能继续达标，那就出去
                    sum -= nums[l++];
                }
                // 记录这一次的最小长度
                if (sum >= target) {
                    ans = Math.min(ans, r - l + 1);
                }
            }
            return ans == Integer.MAX_VALUE ? 0 : ans;
    }





}



