package question5;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;  // Ensure this import is included
import java.util.ArrayList;  // You might also need this for creating ArrayLists
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class NetworkOptimizerApp {

    // NetworkGraph class representing the network structure
    static class NetworkGraph {
        private final Map<String, Map<String, Edge>> adjacencyList = new HashMap<>();

        public void addNode(String nodeName) {
            adjacencyList.putIfAbsent(nodeName, new HashMap<>());
        }

        public void addEdge(String from, String to, int cost, int bandwidth) {
            adjacencyList.get(from).put(to, new Edge(cost, bandwidth));
            adjacencyList.get(to).put(from, new Edge(cost, bandwidth)); // undirected graph
        }

        public Map<String, Map<String, Edge>> getAdjacencyList() {
            return adjacencyList;
        }
    }

    // Edge class to store cost and bandwidth for each connection
    static class Edge {
        int cost;
        int bandwidth;

        Edge(int cost, int bandwidth) {
            this.cost = cost;
            this.bandwidth = bandwidth;
        }
    }

    // GraphAlgorithm class to implement Dijkstra and Prim's algorithms
    static class GraphAlgorithm {

        // Dijkstra's algorithm for shortest path considering bandwidth as weight
        public static List<String> dijkstra(NetworkGraph graph, String start, String end) {
            Map<String, Integer> distances = new HashMap<>();
            Map<String, String> previousNodes = new HashMap<>();
            PriorityQueue<String> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

            for (String node : graph.getAdjacencyList().keySet()) {
                distances.put(node, Integer.MAX_VALUE);
            }
            distances.put(start, 0);
            priorityQueue.add(start);

            while (!priorityQueue.isEmpty()) {
                String current = priorityQueue.poll();
                if (current.equals(end)) break;

                for (Map.Entry<String, Edge> entry : graph.getAdjacencyList().get(current).entrySet()) {
                    String neighbor = entry.getKey();
                    Edge edge = entry.getValue();
                    int newDist = distances.get(current) + edge.cost;

                    if (newDist < distances.get(neighbor)) {
                        distances.put(neighbor, newDist);
                        previousNodes.put(neighbor, current);
                        priorityQueue.add(neighbor);
                    }
                }
            }

            // Reconstruct path
            List<String> path = new ArrayList<>();
            String current = end;
            while (current != null) {
                path.add(current);
                current = previousNodes.get(current);
            }
            Collections.reverse(path);
            return path;
        }
    }

    // NetworkGUI class for creating the user interface
    static class NetworkGUI extends JFrame {
        private final NetworkGraph graph;
        private final JTextArea displayArea;

        public NetworkGUI(NetworkGraph graph) {
            this.graph = graph;
            this.displayArea = new JTextArea(10, 40);
            displayArea.setEditable(false);

            setTitle("Network Optimizer");
            setLayout(new BorderLayout());

            // Add nodes, edges input panel
            JPanel inputPanel = new JPanel();
            inputPanel.setLayout(new FlowLayout());
            JTextField nodeField = new JTextField(10);
            JButton addNodeButton = new JButton("Add Node");
            JButton addEdgeButton = new JButton("Add Edge");

            inputPanel.add(new JLabel("Node Name:"));
            inputPanel.add(nodeField);
            inputPanel.add(addNodeButton);
            inputPanel.add(addEdgeButton);

            // Add action listeners
            addNodeButton.addActionListener(e -> {
                String nodeName = nodeField.getText();
                if (!nodeName.isEmpty()) {
                    graph.addNode(nodeName);
                    displayArea.append("Node added: " + nodeName + "\n");
                    nodeField.setText("");
                }
            });

            addEdgeButton.addActionListener(e -> {
                String fromNode = nodeField.getText();
                String toNode = JOptionPane.showInputDialog("Enter destination node:");
                int cost = Integer.parseInt(JOptionPane.showInputDialog("Enter cost:"));
                int bandwidth = Integer.parseInt(JOptionPane.showInputDialog("Enter bandwidth:"));
                graph.addEdge(fromNode, toNode, cost, bandwidth);
                displayArea.append("Edge added: " + fromNode + " <-> " + toNode + " (Cost: " + cost + ", Bandwidth: " + bandwidth + ")\n");
                nodeField.setText("");
            });

            // Add display area for results
            JScrollPane scrollPane = new JScrollPane(displayArea);
            add(scrollPane, BorderLayout.CENTER);
            add(inputPanel, BorderLayout.NORTH);

            // Set window size and visibility
            setSize(600, 400);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);
        }
    }

    // Main function to run the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NetworkGraph networkGraph = new NetworkGraph();
            new NetworkGUI(networkGraph);
        });
    }
}