package org.example.lab6;

import java.awt.Point;
import java.util.*;

public class MST {

    public static double calculateDistance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    public static double calculateMST(List<Point> dots) {
        int n = dots.size();
        if (n <= 1) return 0;

        // Folosim un heap de priorități pentru a obține latura cu cel mai mic cost
        boolean[] inMST = new boolean[n];
        double totalWeight = 0;
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingDouble(e -> e.weight));

        // Începem de la primul punct
        inMST[0] = true;
        for (int i = 1; i < n; i++) {
            pq.add(new Edge(0, i, calculateDistance(dots.get(0), dots.get(i))));
        }

        while (!pq.isEmpty()) {
            Edge edge = pq.poll();
            if (!inMST[edge.v]) {
                inMST[edge.v] = true;
                totalWeight += edge.weight;

                // Adăugăm toate laturile ce pleacă de la acest vârf
                for (int i = 0; i < n; i++) {
                    if (!inMST[i]) {
                        pq.add(new Edge(edge.v, i, calculateDistance(dots.get(edge.v), dots.get(i))));
                    }
                }
            }
        }

        return totalWeight;
    }

    public static List<List<Edge>> generateSpanningTrees(List<Point> dots, int numTrees) {
        List<List<Edge>> spanningTrees = new ArrayList<>();
        int n = dots.size();

        // Generate multiple spanning trees (starting from different nodes)
        for (int i = 0; i < numTrees; i++) {
            List<Edge> mst = generateMSTFromRandomStart(dots);
            spanningTrees.add(mst);
        }

        return spanningTrees;
    }

    private static List<Edge> generateMSTFromRandomStart(List<Point> dots) {
        int n = dots.size();
        boolean[] inMST = new boolean[n];
        List<Edge> mst = new ArrayList<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingDouble(e -> e.weight));

        // Start from a random node
        Random random = new Random();
        int startNode = random.nextInt(n);
        inMST[startNode] = true;

        for (int i = 0; i < n; i++) {
            if (i != startNode) {
                pq.add(new Edge(startNode, i, calculateDistance(dots.get(startNode), dots.get(i))));
            }
        }
        while (!pq.isEmpty()) {
            Edge edge = pq.poll();
            if (!inMST[edge.v]) {
                inMST[edge.v] = true;
                mst.add(edge);

                // Add edges from the newly added vertex
                for (int i = 0; i < n; i++) {
                    if (!inMST[i]) {
                        pq.add(new Edge(edge.v, i, calculateDistance(dots.get(edge.v), dots.get(i))));
                    }
                }
            }
        }

        return mst;
    }

    public static double calculateTotalWeight(List<Edge> edges, List<Point> dots) {
        double totalWeight = 0;
        for (Edge edge : edges) {
            totalWeight += edge.weight;
        }
        return totalWeight;
    }

    public static List<Edge> getBestSpanningTree(List<List<Edge>> spanningTrees, List<Point> dots) {
        List<Edge> bestTree = null;
        double minCost = Double.MAX_VALUE;

        for (List<Edge> tree : spanningTrees) {
            double cost = calculateTotalWeight(tree, dots);
            if (cost < minCost) {
                minCost = cost;
                bestTree = tree;
            }
        }

        return bestTree;
    }

    public static List<Edge> getWorstSpanningTree(List<List<Edge>> spanningTrees, List<Point> dots) {
        List<Edge> worstTree = null;
        double maxCost = Double.MIN_VALUE;

        for (List<Edge> tree : spanningTrees) {
            double cost = calculateTotalWeight(tree, dots);
            if (cost > maxCost) {
                maxCost = cost;
                worstTree = tree;
            }
        }

        return worstTree;
    }

    static class Edge {
        int u, v;
        double weight;

        Edge(int u, int v, double weight) {
            this.u = u;
            this.v = v;
            this.weight = weight;
        }
    }
}
