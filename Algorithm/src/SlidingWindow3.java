public class SlidingWindow3 {
    // 滑动窗口求子数组的个数


    // 求更长的子串
    // 一般在内部循环技术使用ans = ans + left统计
    // 条件中出现至少:都是cnt>/>=k，while中满徐这个条件持续缩小，知道不满足才开始上面的统计

    //1358
    public int numberOfSubstrings(String s) {
        char[] chars = s.toCharArray();
        int n = chars.length;
        int ans = 0;
        int cnt_a = 0;
        int cnt_b = 0;
        int cnt_c = 0;
        for(int left=0, right=0; right<n; right++){
            if(chars[right]== 'a'){cnt_a++;}
            else if(chars[right]== 'b'){cnt_b++;}
            else if(chars[right]== 'c'){cnt_c++;} // 一直左移并统计个数
            while(cnt_a>0&&cnt_b>0&&cnt_c>0){// 缩小窗口直到不满足全都出现一次，即有些没有出现
                // 同时 不满足条件也不会导致缩小窗口，指挥扩大
                // 当不满足条件时(cnt<=0),结束循环进行统计
                if(chars[left]=='a'){cnt_a--;left++;}
                else if(chars[left]=='b'){cnt_b--;left++;}
                else if(chars[left]=='c'){cnt_c--;left++;}
            }
            ans = ans + left; // 统计结果 目前到达下一个不满足，也就是连续加上左边的就满足
            // 0 ~ left -1 =》共left个
        }
        return ans;
    }

    public long countSubarrays(int[] nums, int k) {
        int max_item = Integer.MIN_VALUE;
        for(int i :nums){max_item = Math.max(i,max_item);}
        long ans = 0;
        int cntMx = 0, left = 0;
        for (int x : nums) {
            if (x == max_item) {
                cntMx++;
            }
            while (cntMx >= k) { //至少出现k此，所以在cnt>=k使持续缩小
                if (nums[left++] == max_item) {
                    cntMx--;
                }
            }
            ans += left;
        }
        return ans;
    }


    // 越短越合法
    // 就是吧最后的统计改为ans = ans + right - left + 1;
    // 依然还是满足条件不进入循环缩小窗口 因此while中的条件使题目条件的取反
    // 713
    public int numSubarrayProductLessThanK(int[] nums, int k) {
        if(k<=1) {
            return 0;
        }
        int ans = 0;
        int sum = 1;
        for(int left = 0, right = 0; right < nums.length; right++){
            sum = sum*nums[right];
            while(sum>=k){
                sum = sum/nums[left];
                left++;
            }
            ans = ans + right - left + 1;
        }
        return ans;
    }

    // 3258
    public int countKConstraintSubstrings(String s, int k) {
        char[] chars = s.toCharArray();
        int[] cnt = new int[2];
        int n = chars.length;
        int ans = 0;
        for(int left = 0, right = 0; right < n; right++){
            if(chars[right]== '0') {cnt[0]++;}
            else {cnt[1]++;}
            while(cnt[0]>k&&cnt[1]>k){
                if(chars[left]=='0') {cnt[0]--;}
                else {cnt[1]--;}
                left++;
            }
            ans = ans + right - left + 1;
        }
        return ans;
    }

    // 2302
    public long countSubarrays(int[] nums, long k) {
        long ans = 0L, sum = 0L;
        for (int left = 0, right = 0; right < nums.length; right++) {
            sum += nums[right];
            while (sum * (right - left + 1) >= k)
                sum -= nums[left++];
            ans += right - left + 1;
        }
        return ans;
    }
}
