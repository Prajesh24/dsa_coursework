public class CriticalTemperature {
    public static int minMeasurements(int k, int n) {
        // Step 1: Initialize test count
        int m = 0;

        // Step 2: Create an array to store maximum levels we can check
        int[][] dp = new int[k + 1][n + 1];

        // Step 3: Increment tests until all temperature levels are covered
        while (dp[k][m] < n) {
            m++; 
            for (int i = 1; i <= k; i++) {
                dp[i][m] = dp[i - 1][m - 1] + dp[i][m - 1] + 1;
            }
        }
        return m;
    }
    public static void main(String[] args) {
        int k1 = 1, n1 = 2;
        int k2 = 2, n2 = 6;
        int k3 = 3, n3 = 14;

        System.out.println("Goal: Find the minimum number of tests required to determine the critical temperature.");
        System.out.println("For k = " + k1 + ", n = " + n1 + " → Minimum tests needed: " + minMeasurements(k1, n1));
        System.out.println("For k = " + k2 + ", n = " + n2 + " → Minimum tests needed: " + minMeasurements(k2, n2));
        System.out.println("For k = " + k3 + ", n = " + n3 + " → Minimum tests needed: " + minMeasurements(k3, n3));
    }
}