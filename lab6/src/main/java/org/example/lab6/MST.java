package org.example.lab6;

import java.awt.Point;
import java.util.*;

public class MST {

    // Calculează distanța euclidiană între două puncte
    public static double calculateDistance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    // Algoritmul lui Prim pentru calcularea MST-ului
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

    // Clasă internă pentru a reprezenta o muchie (linie)
    private static class Edge {
        int u, v;
        double weight;

        Edge(int u, int v, double weight) {
            this.u = u;
            this.v = v;
            this.weight = weight;
        }
    }
}
