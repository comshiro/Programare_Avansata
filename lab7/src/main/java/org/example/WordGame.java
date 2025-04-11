package org.example;

import java.util.Arrays;
import java.util.List;

// Main class: To simulate the game
public class WordGame {
    public static void main(String[] args) throws InterruptedException {
        // Create players
        Player player1 = new Player("Player 1", new TileBag());
        Player player2 = new Player("Player 2", new TileBag());
        Player player3 = new Player("Player 3", new TileBag());

        // List of players
        List<Player> players = Arrays.asList(player1, player2, player3);

        // Initialize the game with the players
        GameBoard gameBoard = new GameBoard(players);

        // Start the game
        gameBoard.startGame();
    }
}
