import java.util.Arrays;

public class SlidingWindow1 {
    /** 力扣643
     * 给你一个由 n 个元素组成的整数数组 nums 和一个整数 k 。
     * 请你找出平均数最大且 长度为 k 的连续子数组，并输出该最大平均数。
     * 任何误差小于 10-5 的答案都将被视为正确答案。
     */
    public double findMaxAverage(int[] nums, int k) {
        double max_Avg = Integer.MIN_VALUE;
        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            if(i<k-1)
            {
                continue;
            }
            max_Avg = Math.max(max_Avg, (double) sum /k);
            sum = sum - nums[i-k+1];

        }
        return max_Avg;
    }

    /** 力扣 1343
     *给你一个整数数组 arr 和两个整数 k 和 threshold 。
     * 请你返回长度为 k 且平均值大于等于 threshold 的子数组数目。
     */
    public int numOfSubarrays(int[] arr, int k, int threshold) {
        int avg = 0;
        int sum = 0;
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            if (i < k-1){
                continue;
            }
            avg = sum / k;
            if(avg >= threshold) {count++;}
            sum = sum - arr[i-k+1];
        }
        return count;
    }

    /**力扣2090  相当于2k+1的窗口求平均值
     * 给你一个下标从 0 开始的数组 nums ，数组中有 n 个整数，另给你一个整数 k 。
     * 半径为 k 的子数组平均值 是指：nums 中一个以下标 i 为 中心 且 半径 为 k 的子数组中所有元素的平均值，即下标在 i - k 和 i + k 范围（含 i - k 和 i + k）内所有元素的平均值。如果在下标 i 前或后不足 k 个元素，那么 半径为 k 的子数组平均值 是 -1 。
     * 构建并返回一个长度为 n 的数组 avgs ，其中 avgs[i] 是以下标 i 为中心的子数组的 半径为 k 的子数组平均值 。
     * x 个元素的 平均值 是 x 个元素相加之和除以 x ，此时使用截断式 整数除法 ，即需要去掉结果的小数部分。
     * 例如，四个元素 2、3、1 和 5 的平均值是 (2 + 3 + 1 + 5) / 4 = 11 / 4 = 2.75，截断后得到 2 。
     */
    public int[] getAverages1(int[] nums, int k) {
        int n = nums.length;
        int[] avgs = new int[n];
        Arrays.fill(avgs, -1);
        long s = 0; // 维护窗口元素和
        for (int i = 0; i < n; i++) {
            // 1. 进入窗口
            s += nums[i];
            if (i < k * 2) { // 窗口大小不足 2k+1
                continue;
            }
            // 2. 记录答案
            avgs[i - k] = (int) (s / (k * 2 + 1));
            // 3. 离开窗口
            s -= nums[i - k * 2];
        }
        return avgs;
    }
    // 这个暴力也没什么问题，但是求和是要用long
    public int[] getAverages(int[] nums, int k) {
        int[] ans = new int[nums.length];
        for(int i = 0; i < nums.length; i++) {
            if (i < k || i>nums.length-k) {
                ans[i] = -1;
                continue;
            }
            int sum = 0;
            for(int j = i-k; j <= i+k; j++) {
                sum += nums[j];
            }
            ans[i] = sum /((2*k)+1);
        }
        return ans;
    }

    /** 力扣2379
     * 给你一个长度为 n 下标从 0 开始的字符串 blocks ，blocks[i] 要么是 'W' 要么是 'B' ，表示第 i 块的颜色。字符 'W' 和 'B' 分别表示白色和黑色。
     * 给你一个整数 k ，表示想要 连续 黑色块的数目。
     * 每一次操作中，你可以选择一个白色块将它 涂成 黑色块。
     * 请你返回至少出现 一次 连续 k 个黑色块的 最少 操作次数
     */
    public int minimumRecolors(String blocks, int k) {
        char[] chars = blocks.toCharArray();
        int min = Integer.MAX_VALUE;
        int ans = 0;
        for(int i = 0; i < blocks.length(); i++) {
            if(chars[i] == 'W') {
                ans++;
            }
            if (i < k - 1) {
                continue;
            }
            min = Math.min(min, ans);
            if(chars[i-k+1] == 'W') {
                ans--;
            }
        }
        return min;
    }

    /** 力扣1052
     * 有一个书店老板，他的书店开了 n 分钟。每分钟都有一些顾客进入这家商店。给定一个长度为 n 的整数数组 customers ，其中 customers[i] 是在第 i 分钟开始时进入商店的顾客数量，所有这些顾客在第 i 分钟结束后离开。
     * 在某些分钟内，书店老板会生气。 如果书店老板在第 i 分钟生气，那么 grumpy[i] = 1，否则 grumpy[i] = 0。
     * 当书店老板生气时，那一分钟的顾客就会不满意，若老板不生气则顾客是满意的。
     * 书店老板知道一个秘密技巧，能抑制自己的情绪，可以让自己连续 minutes 分钟不生气，但却只能使用一次。
     * 请你返回 这一天营业下来，最多有多少客户能够感到满意 。
     */
    public int maxSatisfied(int[] customers, int[] grumpy, int minutes){
        int core = 0;
        for(int i = 0; i < customers.length; i++) {
            if(grumpy[i] == 0) {
                core += customers[i];
            }
        }
        int max = Integer.MIN_VALUE;
        for(int i = 0; i < customers.length; i++) {
            if(grumpy[i] == 1) {
                core += customers[i];
            }
            if(i<minutes-1){
                continue;
            }
            max = Math.max(max, core);
            if(grumpy[i-minutes+1] == 1){
                core -= customers[i-minutes+1];
            }
        }
        return max;
    }

}
