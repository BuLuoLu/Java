public class SlidingWindow {

    /**
     * 定长滑动窗口：一般都是求子串的一个max/min值
     * 窗口大小看，为子串的长度
     * 入：下标为 i 的元素进入窗口，更新相关统计量。如果 i<k−1 则重复第一步。
     * 更新：更新答案。一般是更新最大值/最小值。
     * 出：下标为 i−k+1 的元素离开窗口，更新相关统计量。
     */

    public static int maxVowels(String S, int k) {
        char[] s = S.toCharArray();
        int ans = 0;
        int vowel = 0;
        for (int i = 0; i < s.length; i++) {
            // 1. 进入窗口
            if (s[i] == 'a' || s[i] == 'e' || s[i] == 'i' || s[i] == 'o' || s[i] == 'u') {
                vowel++;
            }
            if (i < k - 1) { // 窗口大小不足 k
                continue;
            }
            // 2. 更新答案
            ans = Math.max(ans, vowel);
            // 3. 离开窗口
            char out = s[i - k + 1];
            if (out == 'a' || out == 'e' || out == 'i' || out == 'o' || out == 'u') {
                vowel--;
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        System.out.println(maxVowels("abciiidef",3));

    }
}
