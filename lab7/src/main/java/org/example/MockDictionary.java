package org.example;

import java.util.Set;

public class MockDictionary implements Dictionary {
    @Override
    public void addWord(String word) {

    }

    @Override
    public boolean isWord(String str) {
        return true;
    }

    @Override
    public Set<String> findWordsWithPrefix(String prefix) {
        return Set.of();
    }
}
