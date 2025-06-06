package org.example.ChessServer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameManager {
    private final Map<String, GameSession> games = new HashMap<>();
    private final Map<String, ClientHandler> players = new HashMap<>();

    public synchronized String createNewGame(String playerId) {
        String gameId = "GAME" + System.currentTimeMillis() % 100;
        games.put(gameId, new GameSession(gameId, playerId));
        System.out.println("Game created: " + gameId + " by player: " + playerId);
        return gameId;
    }

    public synchronized boolean createNewGameWithId(String gameId, String playerId) {
        if (games.containsKey(gameId)) return false;
        games.put(gameId, new GameSession(gameId, playerId));
        System.out.println("Game created with custom ID: " + gameId + " by player: " + playerId);
        return true;
    }

    public synchronized String joinGame(String gameId, String playerId) {
        GameSession game = games.get(gameId);
        if (game != null && game.getBlackPlayer() == null) {
            game.setBlackPlayer(playerId);
            System.out.println("Player " + playerId + " joined game " + gameId + " as BLACK");
            return "BLACK";
        }
        System.out.println("Failed to join game " + gameId + " - game null: " + (game == null) +
                ", black player exists: " + (game != null && game.getBlackPlayer() != null));
        return null;
    }

    public GameSession getGameSession(String gameId) {
        return games.get(gameId);
    }

    public void broadcast(String gameId, String message) {
        GameSession game = games.get(gameId);
        if (game != null) {
            ClientHandler whitePlayer = players.get(game.getWhitePlayer());
            ClientHandler blackPlayer = players.get(game.getBlackPlayer());

            System.out.println("Broadcasting to game " + gameId + ": " + message);
            System.out.println("White player handler: " + (whitePlayer != null));
            System.out.println("Black player handler: " + (blackPlayer != null));

            try {
                if (whitePlayer != null) {
                    whitePlayer.out.writeObject(message);
                    whitePlayer.out.flush();
                }
                if (blackPlayer != null) {
                    blackPlayer.out.writeObject(message);
                    blackPlayer.out.flush();
                }
            } catch (IOException e) {
                System.err.println("Error broadcasting message: " + e.getMessage());
            }
        }
    }

    public void registerPlayer(String playerId, ClientHandler handler) {
        players.put(playerId, handler);
        System.out.println("Registered player: " + playerId);
    }

    public void removePlayer(String gameId, String playerId) {
        removePlayer(gameId, playerId, false);
    }

    public void removePlayer(String gameId, String playerId, boolean skipBroadcast) {
        players.remove(playerId);
        GameSession game = games.get(gameId);
        if (game != null && (playerId.equals(game.getWhitePlayer()) || playerId.equals(game.getBlackPlayer()))) {
            if (!skipBroadcast) {
                broadcast(gameId, "OPPONENT_DISCONNECTED");
            }
            games.remove(gameId);
        }
    }

    public MoveResult processMove(String gameId, String playerId, int fromRow, int fromCol, int toRow, int toCol) {
        GameSession game = games.get(gameId);
        if (game != null) {
            return game.makeMove(playerId, fromRow, fromCol, toRow, toCol);
        }
        return new MoveResult(false, "Game not found", null);
    }

    public ClientHandler getPlayerHandler(String playerId) {
        return players.get(playerId);
    }
}