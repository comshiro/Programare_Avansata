package org.example;

import java.util.List;

public class Player implements Runnable {
    private final String name;
    private Game game;
    private List<Tile> tiles;
    private int score = 0;
    private final WordBuilder wordBuilder;

    public Player(String name) {
        this.name = name;
        this.wordBuilder = null; // Temporarily null until game is set
    }

    public void setGame(Game game) {
        this.game = game;
        this.tiles = game.getBag().extractTiles(7);
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    private boolean submitWord() {
        String word = game.getWordBuilder().findValidWord(tiles);
        if (word != null) {
            int wordPoints = game.getWordBuilder().calculatePoints(word, tiles);
            game.getBoard().addWord(this, word);
            score += wordPoints;

            removeUsedLetters(word);
            tiles.addAll(game.getBag().extractTiles(word.length()));
            return true;
        } else {
            // Couldn't form a valid word: discard tiles and draw new ones
            game.getBag().returnTiles(tiles);
            tiles = game.getBag().extractTiles(7);
            return false;
        }
    }

    private void removeUsedLetters(String word) {
        for (char c : word.toCharArray()) {
            for (int i = 0; i < tiles.size(); i++) {
                if (tiles.get(i).letter() == c) {
                    tiles.remove(i);
                    break;
                }
            }
        }
    }

    @Override
    public void run() {
        while (game.isGameRunning() && !game.getBag().isEmpty()) {
            game.waitTurn(this);
            if (!game.isGameRunning()) break;

            try {
                if (!submitWord()) {
                    Thread.sleep(1000); // no word formed
                } else {
                    Thread.sleep(500); // word formed
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            game.endTurn();
        }
        System.out.println(name + " has finished playing. Final score: " + score);
    }
}
