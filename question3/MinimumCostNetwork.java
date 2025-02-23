package question3;

import java.util.*;

public class MinimumCostNetwork {

    // Helper class to represent an edge
    static class Edge {
        int device1, device2, cost;
        
        public Edge(int device1, int device2, int cost) {
            this.device1 = device1;
            this.device2 = device2;
            this.cost = cost;
        }
    }

    // Union-Find data structure to handle cycle detection
    static class UnionFind {
        int[] parent, rank;

        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }

        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX != rootY) {
                if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else {
                    parent[rootY] = rootX;
                    rank[rootX]++;
                }
            }
        }
    }

    public static int minCostToConnectDevices(int n, int[] modules, int[][] connections) {
        // Step 1: Create all edges (including virtual edges for modules)
        List<Edge> edges = new ArrayList<>();
        
        // Add edges for module installation costs (from a virtual super node)
        for (int i = 0; i < n; i++) {
            edges.add(new Edge(n, i, modules[i])); // virtual node 'n' is the super node
        }
        
        // Add edges for device-to-device connections
        for (int[] conn : connections) {
            edges.add(new Edge(conn[0] - 1, conn[1] - 1, conn[2])); // 0-indexed
        }

        // Step 2: Sort edges by cost
        edges.sort(Comparator.comparingInt(e -> e.cost));

        // Step 3: Apply Kruskal's algorithm
        UnionFind uf = new UnionFind(n + 1); // Include the virtual super node
        int totalCost = 0;
        int edgesSelected = 0;

        // Process edges one by one
        for (Edge edge : edges) {
            int device1 = edge.device1;
            int device2 = edge.device2;
            int cost = edge.cost;

            // If the devices are not already connected, take this edge
            if (uf.find(device1) != uf.find(device2)) {
                uf.union(device1, device2);
                totalCost += cost;
                edgesSelected++;
                if (edgesSelected == n) {
                    break;
                }
            }
        }

        return totalCost;
    }

    public static void main(String[] args) {
        int n = 3;
        int[] modules = {1, 2, 2};
        int[][] connections = {
            {1, 2, 1},
            {2, 3, 1}
        };

        int result = minCostToConnectDevices(n, modules, connections);
        System.out.println("Minimum cost to connect all devices: " + result);
    }
}