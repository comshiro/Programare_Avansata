package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Board {
    private final List<String> words = Collections.synchronizedList(new ArrayList<>());

    public void addWord(Player player, String word) {
        synchronized (words) {
            words.add(word);
        }
        System.out.println(player.getName() + " submitted: " + word);
    }

    public List<String> getWords() {
        synchronized (words) {
            return new ArrayList<>(words);
        }
    }

    @Override
    public String toString() {
        synchronized (words) {
            return words.toString();
        }
    }
}
