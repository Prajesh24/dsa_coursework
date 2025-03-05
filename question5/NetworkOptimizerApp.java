
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

class Graph {
    private final Map<String, Map<String, Integer>> adjacencyList = new HashMap<>();

    public void addNode(String node) {
        adjacencyList.putIfAbsent(node, new HashMap<>());
    }

    public void addEdge(String node1, String node2, int cost) {
        adjacencyList.get(node1).put(node2, cost);
        adjacencyList.get(node2).put(node1, cost);
    }

    public Map<String, Map<String, Integer>> getAdjacencyList() {
        return adjacencyList;
    }

    public List<String> dijkstra(String start, String end) {
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        for (String node : adjacencyList.keySet()) {
            distances.put(node, Integer.MAX_VALUE);
            previous.put(node, null);
        }
        distances.put(start, 0);
        queue.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(end)) break;

            for (Map.Entry<String, Integer> neighbor : adjacencyList.get(current).entrySet()) {
                int newDist = distances.get(current) + neighbor.getValue();
                if (newDist < distances.get(neighbor.getKey())) {
                    distances.put(neighbor.getKey(), newDist);
                    previous.put(neighbor.getKey(), current);
                    queue.add(neighbor.getKey());
                }
            }
        }

        List<String> path = new ArrayList<>();
        for (String at = end; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);
        return path.isEmpty() || !path.get(0).equals(start) ? Collections.emptyList() : path;
    }

    public List<String[]> primMST() {
        Set<String> visited = new HashSet<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.cost));
        List<String[]> mstEdges = new ArrayList<>();

        String startNode = adjacencyList.keySet().iterator().next();
        visited.add(startNode);
        for (var entry : adjacencyList.get(startNode).entrySet()) {
            pq.add(new Edge(startNode, entry.getKey(), entry.getValue()));
        }

        while (!pq.isEmpty() && visited.size() < adjacencyList.size()) {
            Edge edge = pq.poll();
            if (visited.contains(edge.to)) continue;

            visited.add(edge.to);
            mstEdges.add(new String[]{edge.from, edge.to, String.valueOf(edge.cost)});

            for (var entry : adjacencyList.get(edge.to).entrySet()) {
                if (!visited.contains(entry.getKey())) {
                    pq.add(new Edge(edge.to, entry.getKey(), entry.getValue()));
                }
            }
        }
        return mstEdges;
    }

    static class Edge {
        String from, to;
        int cost;
        Edge(String from, String to, int cost) { this.from = from; this.to = to; this.cost = cost; }
    }
}

public class NetworkOptimizer extends JFrame {
    private final Graph graph = new Graph();
    private final JTextField nodeField = new JTextField(10);
    private final JTextField node1Field = new JTextField(5);
    private final JTextField node2Field = new JTextField(5);
    private final JTextField costField = new JTextField(5);
    private final JTextArea outputArea = new JTextArea(10, 30);

    public NetworkOptimizer() {
        setTitle("Network Topology Optimizer");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        add(new JLabel("Node:"));
        add(nodeField);
        JButton addNodeButton = new JButton("Add Node");
        add(addNodeButton);

        add(new JLabel("Edge (Node1 - Node2 - Cost):"));
        add(node1Field);
        add(node2Field);
        add(costField);
        JButton addEdgeButton = new JButton("Add Edge");
        add(addEdgeButton);

        JButton findPathButton = new JButton("Find Shortest Path");
        add(findPathButton);

        JButton optimizeButton = new JButton("Optimize (MST)");
        add(optimizeButton);

        outputArea.setEditable(false);
        add(new JScrollPane(outputArea));

        addNodeButton.addActionListener(e -> {
            String node = nodeField.getText().trim();
            if (!node.isEmpty()) {
                graph.addNode(node);
                outputArea.append("Node added: " + node + "\n");
                nodeField.setText("");
            }
        });

        addEdgeButton.addActionListener(e -> {
            String node1 = node1Field.getText().trim();
            String node2 = node2Field.getText().trim();
            String costText = costField.getText().trim();
        
            if (!node1.isEmpty() && !node2.isEmpty() && !costText.isEmpty()) {
                try {
                    int cost = Integer.parseInt(costText); // Ensure valid integer input
                    graph.addEdge(node1, node2, cost);
                    outputArea.append("Edge added: " + node1 + " - " + node2 + " (Cost: " + cost + ")\n");
        
                    // Clear input fields after adding an edge
                    node1Field.setText("");
                    node2Field.setText("");
                    costField.setText("");
        
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid cost! Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please fill all fields before adding an edge.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        

        findPathButton.addActionListener(e -> {
            String start = JOptionPane.showInputDialog(this, "Enter Start Node:");
            String end = JOptionPane.showInputDialog(this, "Enter End Node:");
            if (start != null && end != null) {
                List<String> path = graph.dijkstra(start, end);
                outputArea.append(path.isEmpty() ? "No path found!\n" : "Shortest Path: " + path + "\n");
            }
        });

        optimizeButton.addActionListener(e -> {
            List<String[]> mst = graph.primMST();
            outputArea.append("Optimized Network (MST):\n");
            for (String[] edge : mst) {
                outputArea.append(edge[0] + " - " + edge[1] + " (Cost: " + edge[2] + ")\n");
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NetworkOptimizer::new);
    }
}