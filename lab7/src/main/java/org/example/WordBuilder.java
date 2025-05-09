package org.example;

import java.util.ArrayList;
import java.util.List;

public class WordBuilder {
    private final Dictionary dictionary;

    public WordBuilder(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public String findValidWord(List<Tile> tiles) {
        List<String> validWords = new ArrayList<>();
        StringBuilder currentWord = new StringBuilder();
        // Start building words from each tile recursively
        backtrack(tiles, currentWord, validWords);

        // Return the first valid word with a length >= 7
        return validWords.stream()
                .filter(word -> word.length() >= 7) // Enforce the 7-letter word rule
                .findFirst()
                .orElse(null);
    }

    // Recursive backtracking to try and build all possible words
    private void backtrack(List<Tile> tiles, StringBuilder currentWord, List<String> validWords) {
        // If the current word is valid, add it to the list
        if (currentWord.length() >= 2 && dictionary.isWord(currentWord.toString())) {
            validWords.add(currentWord.toString());
        }

        // Try adding each tile's letter to the current word
        for (int i = 0; i < tiles.size(); i++) {
            Tile tile = tiles.get(i);
            currentWord.append(tile.letter());  // Add current tile's letter
            List<Tile> remainingTiles = new ArrayList<>(tiles);
            remainingTiles.remove(i);  // Remove the used tile from the list
            backtrack(remainingTiles, currentWord, validWords);  // Recurse with the remaining tiles
            currentWord.deleteCharAt(currentWord.length() - 1);  // Backtrack (remove last character)
        }
    }

    // Calculate points for a word based on the tiles used
    public int calculatePoints(String word, List<Tile> tiles) {
        List<Tile> tempTiles = new ArrayList<>(tiles);
        int points = 0;

        for (char c : word.toCharArray()) {
            for (int i = 0; i < tempTiles.size(); i++) {
                if (tempTiles.get(i).letter() == c) {
                    points += tempTiles.get(i).points();
                    tempTiles.remove(i);  // Remove the tile after using it
                    break;
                }
            }
        }
        return points;
    }
}
