package org.example.ChessServer;

import java.io.*;
import java.net.Socket;
import org.example.service.GameService;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final GameManager gameManager;
    ObjectOutputStream out;
    private ObjectInputStream in;
    private String playerId;
    private String gameId;
    private static final GameService gameService = new GameService();

    public ClientHandler(Socket socket, GameManager gameManager) {
        this.clientSocket = socket;
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            while (true) {
                String message = (String) in.readObject();
                System.out.println("Received from " + playerId + ": " + message);
                String[] parts = message.split(":");

                switch (parts[0]) {
                    case "CONNECT":
                        playerId = parts[1];
                        gameManager.registerPlayer(playerId, this);  // Register player with handler
                        out.writeObject("CONNECTED:" + playerId);
                        out.flush();
                        break;

                    case "CREATE_GAME":
                        gameId = gameManager.createNewGame(playerId);
                        out.writeObject("GAME_CREATED:" + gameId);
                        out.flush();
                        break;

                    case "CREATE_GAME_WITH_ID":
                        if (parts.length > 1) {
                            String customGameId = parts[1];
                            boolean created = gameManager.createNewGameWithId(customGameId, playerId);
                            if (created) {
                                gameId = customGameId;
                                out.writeObject("GAME_CREATED:" + customGameId);
                            } else {
                                out.writeObject("ERROR:Game ID already exists");
                            }
                            out.flush();
                        }
                        break;

                    case "JOIN_GAME":
                        gameId = parts[1];
                        String color = gameManager.joinGame(gameId, playerId);
                        if (color != null) {
                            out.writeObject("GAME_JOINED:" + gameId + ":" + color);
                            out.flush();

                            // Send initial board state to both players
                            GameSession session = gameManager.getGameSession(gameId);
                            int[][] board = session.getBoardState();
                            String boardData = serializeBoard(board);

                            // Broadcast the board state and turn info to both players
                            gameManager.broadcast(gameId, "BOARD_UPDATE:" + boardData);
                            gameManager.broadcast(gameId, "TURN_UPDATE:" + (session.isWhiteTurn() ? "WHITE" : "BLACK"));
                        } else {
                            out.writeObject("ERROR:Invalid game ID or game full");
                            out.flush();
                        }
                        break;

                    case "MOVE":
                        if (gameId == null) {
                            out.writeObject("ERROR:Not in a game");
                            out.flush();
                            break;
                        }
                        GameSession session = gameManager.getGameSession(gameId);
                        if (session == null) {
                            out.writeObject("ERROR:Game session not found");
                            out.flush();
                            break;
                        }
                        boolean isWhiteTurn = session.isWhiteTurn();
                        String whitePlayer = session.getWhitePlayer();
                        String blackPlayer = session.getBlackPlayer();
                        if ((isWhiteTurn && !playerId.equals(whitePlayer)) || (!isWhiteTurn && !playerId.equals(blackPlayer))) {
                            out.writeObject("ERROR:Not your turn");
                            out.flush();
                            break;
                        }
                        try {
                            int fromRow = Integer.parseInt(parts[1]);
                            int fromCol = Integer.parseInt(parts[2]);
                            int toRow = Integer.parseInt(parts[3]);
                            int toCol = Integer.parseInt(parts[4]);

                            System.out.println("Processing move from " + playerId + ": (" + fromRow + "," + fromCol + ") to (" + toRow + "," + toCol + ")");

                            MoveResult result = gameManager.processMove(gameId, playerId, fromRow, fromCol, toRow, toCol);
                            if (result.isValid()) {
                                out.writeObject("MOVE_VALID");
                                out.flush();

                                // Broadcast update to both players
                                String boardData = serializeBoard(result.getBoard());
                                gameManager.broadcast(gameId, "BOARD_UPDATE:" + boardData);

                                // Send turn update
                                session = gameManager.getGameSession(gameId); // Refresh session in case turn changed
                                gameManager.broadcast(gameId, "TURN_UPDATE:" + (session.isWhiteTurn() ? "WHITE" : "BLACK"));

                                // Check for game over
                                if (result.isCheckmate()) {
                                    gameManager.broadcast(gameId, "GAME_OVER:CHECKMATE:" + result.getWinner());
                                    // Save game to DB on checkmate
                                    if (session != null) {
                                        String white = session.getWhitePlayer();
                                        String black = session.getBlackPlayer();
                                        String gameResult;
                                        if ("WHITE".equals(result.getWinner())) {
                                            gameResult = "1-0";
                                        } else if ("BLACK".equals(result.getWinner())) {
                                            gameResult = "0-1";
                                        } else {
                                            gameResult = "1/2-1/2"; // fallback for draw
                                        }
                                        session.endGame();
                                        String moveListStr = String.join(",", session.getMoveList());
                                        try {
                                            if (white == null || black == null || white.isEmpty() || black.isEmpty()) {
                                                System.err.println("[ERROR] Cannot save game: white or black player is null or empty. white=" + white + ", black=" + black);
                                            } else {
                                                gameService.saveGame(white, black, gameResult, session.getDuration(), moveListStr);
                                                System.out.println("[DEBUG] Game saved successfully.");
                                            }
                                        } catch (Exception ex) {
                                            System.err.println("[ERROR] saveGame failed: " + ex.getMessage());
                                            ex.printStackTrace();
                                        }
                                    }
                                    gameManager.removePlayer(gameId, playerId); // Remove both players from game
                                } else if (result.isCheck()) {
                                    gameManager.broadcast(gameId, "CHECK:" +
                                            (session.isWhiteTurn() ? "BLACK" : "WHITE"));
                                } else if (result.isDraw() || result.isStalemate()) {
                                    gameManager.broadcast(gameId, "GAME_OVER:DRAW");
                                    if (session != null) {
                                        String white = session.getWhitePlayer();
                                        String black = session.getBlackPlayer();
                                        session.endGame();
                                        String moveListStr = String.join(",", session.getMoveList());
                                        try {
                                            if (white == null || black == null || white.isEmpty() || black.isEmpty()) {
                                                System.err.println("[ERROR] Cannot save game: white or black player is null or empty. white=" + white + ", black=" + black);
                                            } else {
                                                gameService.saveGame(white, black, "1/2-1/2", session.getDuration(), moveListStr);
                                                System.out.println("[DEBUG] Game saved successfully.");
                                            }
                                        } catch (Exception ex) {
                                            System.err.println("[ERROR] saveGame failed: " + ex.getMessage());
                                            ex.printStackTrace();
                                        }
                                    }
                                    gameManager.removePlayer(gameId, playerId);
                                }
                            } else {
                                out.writeObject("MOVE_INVALID:" + result.getMessage());
                                out.flush();
                                System.out.println("Move rejected: " + result.getMessage());
                            }
                        } catch (Exception ex) {
                            System.err.println("[ERROR] Exception during MOVE: " + ex.getMessage());
                            ex.printStackTrace();
                            try {
                                out.writeObject("ERROR:Server error during move: " + ex.getMessage());
                                out.flush();
                            } catch (IOException ignored) {}
                        }
                        break;

                    case "SURRENDER":
                        if (parts.length > 1) {
                            String surrenderGameId = parts[1];
                            session = gameManager.getGameSession(surrenderGameId); // reuse existing session variable
                            if (session != null) {
                                String white = session.getWhitePlayer();
                                String black = session.getBlackPlayer();
                                String gameResult;
                                boolean surrenderingIsWhite = playerId.equals(white);
                                if (surrenderingIsWhite) {
                                    gameResult = "0-1"; // Black wins
                                } else {
                                    gameResult = "1-0"; // White wins
                                }
                                session.endGame();
                                System.out.println("[DEBUG] Saving game: white=" + white + ", black=" + black + ", result=" + gameResult + ", duration=" + session.getDuration() + ", moves=" + session.getMoveList());
                                String moveListStr = String.join(",", session.getMoveList());
                                try {
                                    if (white == null || black == null || white.isEmpty() || black.isEmpty()) {
                                        System.err.println("[ERROR] Cannot save game: white or black player is null or empty. white=" + white + ", black=" + black);
                                    } else {
                                        gameService.saveGame(white, black, gameResult, session.getDuration(), moveListStr);
                                        System.out.println("[DEBUG] Game saved successfully.");
                                    }
                                } catch (Exception ex) {
                                    System.err.println("[ERROR] saveGame failed: " + ex.getMessage());
                                    ex.printStackTrace();
                                }
                                // Notify surrendering player
                                out.writeObject("GAME_OVER:YOU_LOST_BY_SURRENDER");
                                out.flush();
                                // Notify opponent
                                ClientHandler opponentHandler = null;
                                if (surrenderingIsWhite && black != null) {
                                    opponentHandler = gameManager.getPlayerHandler(black);
                                } else if (!surrenderingIsWhite && white != null) {
                                    opponentHandler = gameManager.getPlayerHandler(white);
                                }
                                if (opponentHandler != null) {
                                    opponentHandler.out.writeObject("GAME_OVER:OPPONENT_SURRENDERED");
                                    opponentHandler.out.flush();
                                }
                                // Remove both players from the game, skip broadcast
                                gameManager.removePlayer(surrenderGameId, white, true);
                                gameManager.removePlayer(surrenderGameId, black, true);
                                break;
                            }
                            // If session is null, fallback to old logic
                            gameManager.removePlayer(surrenderGameId, playerId);
                        }
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Client " + playerId + " disconnected: " + e.getMessage());
            if (gameId != null) {
                gameManager.removePlayer(gameId, playerId);
            }
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
        }
    }

    private String serializeBoard(int[][] board) {
        StringBuilder sb = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                sb.append(board[i][j]);
                if (i > 0 || j < 7) sb.append(",");  // Don't add comma after last element
            }
        }
        return sb.toString();
    }
}