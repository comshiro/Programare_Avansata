package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Game {
    private final Bag bag = new Bag();
    private final Board board = new Board();
    private final Dictionary dictionary = new MockDictionary();
    private final List<Player> players = new ArrayList<>();
    private final Object turnLock = new Object();
    private int currentPlayerIndex = 0;
    private boolean gameRunning = true;
    private final long maxDurationMillis = 60_000; // 60 seconds
    private final WordBuilder wordBuilder = new WordBuilder(dictionary);

    public Bag getBag() {
        return bag;
    }

    public Board getBoard() {
        return board;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public synchronized void stopGame() {
        gameRunning = false;
    }

    public void addPlayer(Player player) {
        player.setGame(this);
        players.add(player);
    }

    public void waitTurn(Player player) {
        synchronized (turnLock) {
            while (!players.get(currentPlayerIndex).equals(player) && gameRunning) {
                try {
                    turnLock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    public void endTurn() {
        synchronized (turnLock) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            turnLock.notifyAll();
        }
    }

    public void play() {
        // Start timekeeper
        Thread timekeeper = new Thread(() -> {
            long start = System.currentTimeMillis();
            while (gameRunning) {
                long elapsed = System.currentTimeMillis() - start;
                if (elapsed >= maxDurationMillis) {
                    System.out.println("Time limit reached. Stopping the game!");
                    stopGame();
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        timekeeper.setDaemon(true);
        timekeeper.start();

        // Start players
        List<Thread> threads = new ArrayList<>();
        for (Player player : players) {
            Thread t = new Thread(player);
            threads.add(t);
            t.start();
        }

        // Wait for all threads to finish
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Announce winner
        Player winner = Collections.max(players, Comparator.comparingInt(Player::getScore));
        System.out.println("\n Game Over! The winner is " + winner.getName() + " with " + winner.getScore() + " points!");
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.addPlayer(new Player("Player 1"));
        game.addPlayer(new Player("Player 2"));
        game.addPlayer(new Player("Player 3"));
        game.play();
    }

    public WordBuilder getWordBuilder() {
        return wordBuilder;
    }
}
