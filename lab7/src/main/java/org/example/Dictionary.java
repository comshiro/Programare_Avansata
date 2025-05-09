package org.example;

import java.util.Set;

public interface Dictionary {
    void addWord(String word);
    boolean isWord(String word);
    Set<String> findWordsWithPrefix(String prefix);
}