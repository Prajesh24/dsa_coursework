package question4;
import java.util.*;
public class PackageCollection {
    public static int minRoadsToTraverse(int[] packages, int[][] roads) {
        int n = packages.length;
        // Build adjacency list
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] road : roads) {
            graph.get(road[0]).add(road[1]);
            graph.get(road[1]).add(road[0]);
        }

        int totalPackages = 0;
        for (int val : packages) {
            if (val == 1)
                totalPackages++;
        }
        int fullMask = (1 << totalPackages) - 1; // All packages collected mask

        int minMoves = Integer.MAX_VALUE;

        // Try each node as starting point
        for (int start = 0; start < n; start++) {
            int[] result = findMinMoves(start, packages, graph, fullMask);
            if (result[0] != -1) { // If solution found
                minMoves = Math.min(minMoves, result[0]);
            }
        }

        return minMoves == Integer.MAX_VALUE ? -1 : minMoves;
    }

    private static int[] findMinMoves(int start, int[] packages, List<List<Integer>> graph, int fullMask) {
        int n = packages.length;
        Queue<State> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        // Initial state: at start position, no packages collected
        State initial = new State(start, 0, 0, start);
        queue.offer(initial);
        String initialKey = start + "," + 0 + "," + start;
        visited.add(initialKey);

        while (!queue.isEmpty()) {
            State current = queue.poll();

            // Get all nodes within distance 2
            int collected = current.collected | getCollectable(current.pos, packages, graph);

            // If all packages collected, calculate return cost
            if (collected == fullMask) {
                int returnCost = getDistance(current.pos, current.start, graph);
                if (returnCost != -1) {
                    return new int[] { current.moves + returnCost, collected };
                }
                continue;
            }

            // Try moving to adjacent nodes
            for (int next : graph.get(current.pos)) {
                String key = next + "," + collected + "," + current.start;
                if (!visited.contains(key)) {
                    visited.add(key);
                    queue.offer(new State(next, collected, current.moves + 1, current.start));
                }
            }
        }
        return new int[] { -1, 0 }; // No solution found
    }

    private static int getCollectable(int pos, int[] packages, List<List<Integer>> graph) {
        int collected = 0;
        int packageCount = 0;
        Map<Integer, Integer> distances = getDistances(pos, graph);

        for (int i = 0; i < packages.length; i++) {
            if (packages[i] == 1) {
                if (distances.containsKey(i) && distances.get(i) <= 2) {
                    collected |= (1 << packageCount);
                }
                packageCount++;
            }
        }
        return collected;
    }

    private static Map<Integer, Integer> getDistances(int start, List<List<Integer>> graph) {
        Map<Integer, Integer> distances = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();

        queue.offer(start);
        distances.put(start, 0);
        visited.add(start);

        while (!queue.isEmpty() && distances.get(queue.peek()) < 2) {
            int curr = queue.poll();
            for (int next : graph.get(curr)) {
                if (!visited.contains(next)) {
                    visited.add(next);
                    distances.put(next, distances.get(curr) + 1);
                    queue.offer(next);
                }
            }
        }
        return distances;
    }

    private static int getDistance(int from, int to, List<List<Integer>> graph) {
        if (from == to)
            return 0;

        Queue<Integer> queue = new LinkedList<>();
        Map<Integer, Integer> distances = new HashMap<>();
        Set<Integer> visited = new HashSet<>();

        queue.offer(from);
        distances.put(from, 0);
        visited.add(from);

        while (!queue.isEmpty()) {
            int curr = queue.poll();
            for (int next : graph.get(curr)) {
                if (!visited.contains(next)) {
                    visited.add(next);
                    distances.put(next, distances.get(curr) + 1);
                    if (next == to)
                        return distances.get(next);
                    queue.offer(next);
                }
            }
        }
        return -1;
    }

    static class State {
        int pos; // current position
        int collected; // bitmask of collected packages
        int moves; // number of moves made
        int start; // starting position

        State(int pos, int collected, int moves, int start) {
            this.pos = pos;
            this.collected = collected;
            this.moves = moves;
            this.start = start;
        }
    }

    public static void main(String[] args) {
        // Test case 1
        int[] packages1 = { 1, 1, 1, 1 };
        int[][] roads1 = { { 0, 1 }, { 0, 2 }, { 0, 3 }, { 1, 2 }, { 1, 3 }, { 2, 3 } };
        System.out.println("The minimum number of roads to traverse: "+minRoadsToTraverse(packages1, roads1)); 

        int[] packages4 = { 1, 0, 0, 0, 0, 0, 1 };
        int[][] roads4 = { { 0, 1 }, { 1, 2 }, { 2, 3 }, { 3, 4 }, { 4, 5 }, { 5, 6 } };
        System.out.println("The minimum number of roads to traverse:" + minRoadsToTraverse(packages4, roads4));
    }

    public static int minRoads(int[] packages, int[][] roads) {
        int n = packages.length;
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] road : roads) {
            graph.get(road[0]).add(road[1]);
            graph.get(road[1]).add(road[0]);
        }

        int minRoads = Integer.MAX_VALUE;

        for (int start = 0; start < n; start++) {
            int[] distances = new int[n];
            Arrays.fill(distances, -1);
            Queue<Integer> queue = new LinkedList<>();
            queue.add(start);
            distances[start] = 0;

            // Perform BFS to calculate distances from the starting node
            while (!queue.isEmpty()) {
                int current = queue.poll();
                for (int neighbor : graph.get(current)) {
                    if (distances[neighbor] == -1) {
                        distances[neighbor] = distances[current] + 1;
                        queue.add(neighbor);
                    }
                }
            }

            // Check if all packages can be collected within a distance of 2
            boolean canCollectAll = true;
            for (int i = 0; i < n; i++) {
                if (packages[i] == 1 && distances[i] > 2) {
                    canCollectAll = false;
                    break;
                }
            }

            if (!canCollectAll) {
                continue; // Skip this starting node if not all packages can be collected
            }

            // Calculate the total roads traversed
            int totalRoads = 0;
            for (int i = 0; i < n; i++) {
                if (packages[i] == 1) {
                    totalRoads += distances[i]; // Traverse to the package location
                }
            }
            totalRoads *= 2; // Traverse back to the starting node

            if (totalRoads < minRoads) {
                minRoads = totalRoads;
            }
        }

        return minRoads == Integer.MAX_VALUE ? -1 : minRoads;
    }
}