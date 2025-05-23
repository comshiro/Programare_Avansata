package org.example;

import java.util.HashMap;
import java.util.Map;

public class GameManager {
    private final Map<String, Game> games = new HashMap<>();
    private int gameCounter = 1;

    public synchronized Game createGame(int initialTime) {
        String gameId = "game" + gameCounter++;
        Game game = new Game(gameId, initialTime);
        games.put(gameId, game);
        return game;
    }

    public synchronized Game getGame(String gameId) {
        return games.get(gameId);
    }

    public synchronized void removeGame(String gameId) {
        games.remove(gameId);
    }

    public Map<String, Game> getGames() {
        return games;
    }
}
