package org.example;

import java.util.List;

public class GraphBuilder {
    private final Digraph<TrieNode, Character> digraph = new Digraph<>();

    // Add a word to the Digraph (Trie structure)
    public int addWord(String word) {
        int currentVertex = digraph.addLabeledVertex(new TrieNode("\u0000", false)); // Root
        for (char c : word.toCharArray()) {
            int nextVertex = findOrCreateChild(currentVertex, c);
            currentVertex = nextVertex;
        }
        // Mark the last node as a word node
        TrieNode lastNode = digraph.getVertexLabel(currentVertex);
        digraph.addLabeledVertex(new TrieNode(lastNode.getCharacter(), true));
        return currentVertex;
    }

    // Find or create a child vertex for a given character
    private int findOrCreateChild(int vertex, char c) {
        for (int neighbor : digraph.neighbors(vertex)) {
            if (digraph.getEdgeLabel(vertex, neighbor) == c) {
                return neighbor;
            }
        }
        // Create a new child vertex
        TrieNode newNode = new TrieNode(String.valueOf(c), false);
        int newVertex = digraph.addLabeledVertex(newNode);
        digraph.addLabeledEdge(vertex, newVertex, c);
        return newVertex;
    }

    // Build the Digraph from a dictionary (list of words)
    public Digraph<TrieNode, Character> buildDigraph(List<String> dictionary) {
        for (String word : dictionary) {
            addWord(word);
        }
        return digraph;
    }

    // Static method to return an empty GraphBuilder instance
    public static GraphBuilder empty() {
        return new GraphBuilder(); // Creates a new empty GraphBuilder
    }
}
