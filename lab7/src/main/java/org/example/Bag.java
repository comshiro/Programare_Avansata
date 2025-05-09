package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Bag {
    private final List<Tile> tiles;

    public Bag() {
        this.tiles = new ArrayList<>();
        initializeTiles();
    }

    private void initializeTiles() {
        // Create 10 tiles for each letter (a-z) with random points (1-10)
        for (char c = 'a'; c <= 'z'; c++) {
            int points = (int) (Math.random() * 10) + 1; // Random points between 1-10
            for (int i = 0; i < 10; i++) {
                tiles.add(new Tile(c, points));
            }
        }
        Collections.shuffle(tiles);
    }

    public synchronized List<Tile> extractTiles(int howMany) {
        List<Tile> extracted = new ArrayList<>();
        for (int i = 0; i < howMany; i++) {
            if (tiles.isEmpty()) {
                break;
            }
            extracted.add(tiles.remove(0));
        }
        return extracted;
    }

    public synchronized void returnTiles(List<Tile> returnedTiles) {
        tiles.addAll(returnedTiles);
        Collections.shuffle(tiles);
    }

    public synchronized boolean isEmpty() {
        return tiles.isEmpty();
    }

    public synchronized int remainingTiles() {
        return tiles.size();
    }
}