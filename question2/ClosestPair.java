package question2;

public class ClosestPair {
    public static int[] findClosestPair(int[] x_coords, int[] y_coords) {
        int n = x_coords.length;
        int minDistance = Integer.MAX_VALUE;
        int[] closestPair = new int[2]; // To store the pair of indices (i, j)

        // Step 1: Brute force compare all pairs of points
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Calculate the Manhattan distance
                int distance = Math.abs(x_coords[i] - x_coords[j]) + Math.abs(y_coords[i] - y_coords[j]);
                
                // Step 2: Check if the current pair has a smaller distance or is lexicographically smaller
                if (distance < minDistance) {
                    minDistance = distance;
                    closestPair[0] = i;
                    closestPair[1] = j;
                } else if (distance == minDistance && i < closestPair[0] || (i == closestPair[0] && j < closestPair[1])) {
                    closestPair[0] = i;
                    closestPair[1] = j;
                }
            }
        }

        return closestPair;
    }

    public static void main(String[] args) {
        int[] x_coords = {1, 2, 3, 2, 4};
        int[] y_coords = {2, 3, 1, 2, 3};

        // Goal: Find the lexicographically smallest pair of points with the smallest distance
        int[] result = findClosestPair(x_coords, y_coords);
        
        System.out.println("The indices of the closest pair of points are: [" + result[0] + ", " + result[1] + "]");
    }
}