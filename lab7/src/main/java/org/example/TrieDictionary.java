package org.example;

import java.util.*;

public class TrieDictionary implements Dictionary {
    public record TrieNode(String text, boolean isWord, Map<Character, Integer> children) {
        public String getCharacter() {
            if (text.isEmpty()) return "";
            return String.valueOf(text.charAt(text.length() - 1));
        }
    }

    private final Map<Integer, TrieNode> nodes = new HashMap<>();
    private int nextId = 1; // Start IDs from 1 since 0 is the root

    public TrieDictionary() {
        nodes.put(0, new TrieNode("", false, new HashMap<>())); // root node
    }

    @Override
    public void addWord(String word) {
        int current = 0;
        for (char c : word.toCharArray()) {
            TrieNode node = nodes.get(current);
            Map<Character, Integer> children = new HashMap<>(node.children());
            if (!children.containsKey(c)) {
                TrieNode newNode = new TrieNode(node.text() + c, false, new HashMap<>());
                nodes.put(nextId, newNode);
                children.put(c, nextId);
                node = new TrieNode(node.text(), node.isWord(), children);
                nodes.put(current, node);
                nextId++;
            }
            current = children.get(c);
        }
        TrieNode finalNode = nodes.get(current);
        nodes.put(current, new TrieNode(finalNode.text(), true, finalNode.children()));
    }

    @Override
    public boolean isWord(String word) {
        int current = 0;
        for (char c : word.toCharArray()) {
            TrieNode node = nodes.get(current);
            if (!node.children().containsKey(c)) return false;
            current = node.children().get(c);
        }
        return nodes.get(current).isWord();
    }

    @Override
    public Set<String> findWordsWithPrefix(String prefix) {
        Set<String> result = new HashSet<>();
        int current = 0;
        for (char c : prefix.toCharArray()) {
            TrieNode node = nodes.get(current);
            if (!node.children().containsKey(c)) return result;
            current = node.children().get(c);
        }
        dfs(current, result);
        return result;
    }

    private void dfs(int id, Set<String> result) {
        TrieNode node = nodes.get(id);
        if (node.isWord()) {
            result.add(node.text());
        }
        for (int childId : node.children().values()) {
            dfs(childId, result);
        }
    }
}