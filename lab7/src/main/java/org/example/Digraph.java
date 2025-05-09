package org.example;

import java.util.*;

public class Digraph<V, E> {
    private final Map<Integer, V> vertexLabels = new HashMap<>();
    private final Map<Integer, Map<Integer, E>> edges = new HashMap<>();
    private int vertexIdCounter = 0; // To generate unique IDs for vertices

    // Add a labeled vertex to the graph
    public int addLabeledVertex(V label) {
        int vertexId = vertexIdCounter++;
        vertexLabels.put(vertexId, label);
        edges.put(vertexId, new HashMap<>());
        return vertexId;
    }

    // Add a labeled edge from vertex v to vertex u with edge label
    public void addLabeledEdge(int v, int u, E edgeLabel) {
        edges.get(v).put(u, edgeLabel);
    }

    // Get the label of a vertex
    public V getVertexLabel(int v) {
        return vertexLabels.get(v);
    }

    // Get the neighbors of a vertex
    public Iterable<Integer> neighbors(int v) {
        return edges.get(v).keySet();
    }

    // Get the edge label between two vertices
    public E getEdgeLabel(int v, int u) {
        return edges.get(v).get(u);
    }

    // Check if a vertex has neighbors
    public boolean hasNeighbors(int v) {
        return !edges.get(v).isEmpty();
    }

    // Get the total number of vertices in the graph
    public int vertexCount() {
        return vertexLabels.size();
    }
}
