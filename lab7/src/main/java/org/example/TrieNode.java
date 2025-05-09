package org.example;

public record TrieNode(String text, boolean isWord) {
    public String getCharacter() {
        if (text.isEmpty()) return "";
        return String.valueOf(text.charAt(text.length() - 1));
    }
}
