import java.util.PriorityQueue;
import java.util.HashSet;

public class KthLowestInvestment {
    static class Pair {
        int i, j, value;

        public Pair(int i, int j, int value) {
            this.i = i;
            this.j = j;
            this.value = value;
        }
    }

    public static int kthLowestReturn(int[] returns1, int[] returns2, int k) {
        PriorityQueue<Pair> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a.value, b.value));
        HashSet<String> visited = new HashSet<>();

        // Insert the first combination (smallest product)
        minHeap.add(new Pair(0, 0, returns1[0] * returns2[0]));
        visited.add("0,0");

        int result = 0;

        // Extract k times
        while (k-- > 0) {
            Pair current = minHeap.poll();
            result = current.value;
            int i = current.i, j = current.j;

            // Try inserting (i+1, j) if not visited
            if (i + 1 < returns1.length && !visited.contains((i + 1) + "," + j)) {
                minHeap.add(new Pair(i + 1, j, returns1[i + 1] * returns2[j]));
                visited.add((i + 1) + "," + j);
            }

            // Try inserting (i, j+1) if not visited
            if (j + 1 < returns2.length && !visited.contains(i + "," + (j + 1))) {
                minHeap.add(new Pair(i, j + 1, returns1[i] * returns2[j + 1]));
                visited.add(i + "," + (j + 1));
            }
        }

        return result;
    }

    public static void main(String[] args) {
        int[] returns1 = {2, 5};
        int[] returns2 = {3, 4};
        int k1 = 2;

        int[] returns3 = {-4, -2, 0, 3};
        int[] returns4 = {2, 4};
        int k2 = 6;

        System.out.println("\nGoal: Find the k-th smallest combined return by selecting one investment from each array.");
        System.out.println("For returns1 = [2,5], returns2 = [3,4], k = " + k1 + " → k-th smallest return: " + kthLowestReturn(returns1, returns2, k1));
        System.out.println("For returns1 = [-4,-2,0,3], returns2 = [2,4], k = " + k2 + " → k-th smallest return: " + kthLowestReturn(returns3, returns4, k2));
    }
}