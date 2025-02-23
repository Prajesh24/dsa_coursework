package question2;

import java.util.Arrays;

public class MinimumRewards {
    public static int minRewards(int[] ratings) {
        int n = ratings.length;
        if (n == 0) return 0;  // Edge case

        int[] rewards = new int[n];
        Arrays.fill(rewards, 1); // Every employee gets at least 1 reward initially

        // Left-to-Right Pass
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                rewards[i] = rewards[i - 1] + 1;
            }
        }

        // Right-to-Left Pass
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1);
            }
        }

        // Total rewards needed
        int totalRewards = Arrays.stream(rewards).sum();
        return totalRewards;
    }

    public static void main(String[] args) {
        int[] ratings1 = {1, 2, 2};
        int[] ratings2 = {1, 0, 2};
        int[] ratings3 = {1, 3, 2, 2, 1};

        System.out.println("Goal: Find the minimum number of rewards required for employees based on performance.");
        System.out.println("For ratings = [1, 2, 2] → Minimum rewards needed: " + minRewards(ratings1));
        System.out.println("For ratings = [1, 0, 2] → Minimum rewards needed: " + minRewards(ratings2));
        System.out.println("For ratings = [1, 3, 2, 2, 1] → Minimum rewards needed: " + minRewards(ratings3));
    }
}