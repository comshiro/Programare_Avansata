package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientThread implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(ClientThread.class.getName());
    private static final GameManager gameManager = new GameManager();

    private final Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public ClientThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            // Set up communication streams
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            // Read commands from client until connection is closed or "stop" command received
            while ((inputLine = in.readLine()) != null) {
                LOGGER.info("Received command: " + inputLine);

                String response = processCommand(inputLine);

                out.println(response);

                if (inputLine.equalsIgnoreCase("stop")) {
                    break;
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error handling client communication", e);
        } finally {
            closeConnection();
        }
    }

    private String processCommand(String command) {
        StringTokenizer tokenizer = new StringTokenizer(command);
        if (!tokenizer.hasMoreTokens()) return "Invalid command";
        String cmd = tokenizer.nextToken().toLowerCase();
        switch (cmd) {
            case "create":
                if (tokenizer.hasMoreTokens() && tokenizer.nextToken().equalsIgnoreCase("game")) {
                    if (tokenizer.countTokens() >= 2) {
                        String playerName = tokenizer.nextToken();
                        int time;
                        try {
                            time = Integer.parseInt(tokenizer.nextToken());
                        } catch (NumberFormatException e) {
                            return "Invalid time format";
                        }
                        Game game = gameManager.createGame(time);
                        Player player = new Player(playerName, time);
                        game.addPlayer(player);
                        return "Game created with ID: " + game.getGameId();
                    } else {
                        return "Usage: create game <playerName> <timeInSeconds>";
                    }
                }
                break;
            case "join":
                if (tokenizer.hasMoreTokens() && tokenizer.nextToken().equalsIgnoreCase("game")) {
                    if (tokenizer.countTokens() >= 2) {
                        String gameId = tokenizer.nextToken();
                        String playerName = tokenizer.nextToken();
                        Game game = gameManager.getGame(gameId);
                        if (game == null) return "Game not found";
                        if (game.getPlayers().size() >= 2) return "Game already has 2 players";
                        Player player = new Player(playerName, game.getPlayers().isEmpty() ? game.getCurrentPlayer().getTimeLeft() : game.getPlayers().get(0).getTimeLeft());
                        game.addPlayer(player);
                        if (game.getPlayers().size() == 2) {
                            game.startGame();
                            return "Joined game " + gameId + ". Game started!";
                        }
                        return "Joined game " + gameId + ". Waiting for another player...";
                    } else {
                        return "Usage: join game <gameId> <playerName>";
                    }
                }
                break;
            case "ai":
                if (tokenizer.hasMoreTokens() && tokenizer.nextToken().equalsIgnoreCase("game")) {
                    if (tokenizer.countTokens() >= 2) {
                        String playerName = tokenizer.nextToken();
                        int time;
                        try {
                            time = Integer.parseInt(tokenizer.nextToken());
                        } catch (NumberFormatException e) {
                            return "Invalid time format";
                        }
                        Game game = gameManager.createGame(time);
                        Player human = new Player(playerName, time);
                        Player ai = new Player("AI", time);
                        game.addPlayer(human); // RED
                        game.addPlayer(ai);    // BLUE
                        game.startGame();
                        return "AI game created with ID: " + game.getGameId() + ". You play against AI. You are RED, AI is BLUE.";
                    } else {
                        return "Usage: ai game <playerName> <timeInSeconds>";
                    }
                }
                break;
            case "submit":
                if (tokenizer.hasMoreTokens() && tokenizer.nextToken().equalsIgnoreCase("move")) {
                    if (tokenizer.countTokens() >= 4) {
                        String gameId = tokenizer.nextToken();
                        String playerName = tokenizer.nextToken();
                        int row, col;
                        try {
                            row = Integer.parseInt(tokenizer.nextToken());
                            col = Integer.parseInt(tokenizer.nextToken());
                        } catch (NumberFormatException e) {
                            return "Invalid move coordinates";
                        }
                        Game game = gameManager.getGame(gameId);
                        if (game == null) return "Game not found";
                        if (!game.isStarted()) return "Game not started yet";
                        Player currentPlayer = game.getCurrentPlayer();
                        if (!currentPlayer.getName().equals(playerName)) return "Not your turn";
                        int elapsed = game.updateAndGetElapsedTime();
                        currentPlayer.decrementTime(elapsed);
                        if (currentPlayer.getTimeLeft() <= 0) {
                            game.setFinished();
                            return "Time out! Player " + playerName + " lost.";
                        }
                        boolean valid = game.makeHexMove(playerName, row, col);
                        if (!valid) return "Invalid move. Try again.";
                        StringBuilder sb = new StringBuilder();
                        sb.append("Move accepted. Remaining time: ")
                          .append(currentPlayer.getTimeLeft()).append("s (elapsed: ")
                          .append(elapsed).append("s)\n");
                        sb.append(game.getHexBoard().toString()); // Always show board after human move
                        // If next player is AI and game is not finished, make a single AI move
                        Player nextPlayer = game.getCurrentPlayer();
                        if (nextPlayer.getName().equals("AI") && !game.isFinished()) {
                            HexBoard.Cell aiColor = game.getPlayerColor(nextPlayer.getName()); // Use the color for the current AI player
                            HexAI ai = new HexAI();
                            int[] aiMove = ai.getMove(game.getHexBoard(), aiColor);
                            if (aiMove != null && game.getHexBoard().isValidMove(aiMove[0], aiMove[1])) { // Ensure move is valid
                                int aiElapsed = 1; // AI moves instantly for now
                                nextPlayer.decrementTime(aiElapsed);
                                boolean aiValid = game.makeHexMove(nextPlayer.getName(), aiMove[0], aiMove[1]);
                                if (aiValid) {
                                    sb.append("AI moves at: ").append(aiMove[0]).append(",").append(aiMove[1]).append("\n");
                                    sb.append(game.getHexBoard().toString()); // Show board after AI move
                                    if (game.isFinished()) {
                                        sb.append("Player AI (" + aiColor + ") wins!\n");
                                        return sb.toString();
                                    }
                                } else {
                                    sb.append("AI could not make a valid move.\n");
                                }
                            } else {
                                sb.append("AI could not make a valid move.\n");
                            }
                            // Update nextPlayer after AI move
                            nextPlayer = game.getCurrentPlayer();
                        }
                        if (game.isFinished()) {
                            sb.append("Player ").append(playerName).append(" (" + game.getPlayerColor(playerName) + ") wins!\n");
                            return sb.toString();
                        }
                        sb.append("Next turn: ").append(nextPlayer.getName())
                          .append(" (" + game.getPlayerColor(nextPlayer.getName()) + ")\n");
                        return sb.toString();
                    } else {
                        return "Usage: submit move <gameId> <playerName> <row> <col>";
                    }
                }
                break;
            case "stop":
                shutdownServer();
                return "Server stopped";
            default:
                return "Unknown command";
        }
        return "Invalid command format";
    }

    private void shutdownServer() {
       LOGGER.info("Server shutdown initiated by client");
    }

    private void closeConnection() {
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
                LOGGER.info("Client connection closed: " + clientSocket.getInetAddress());
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error closing client connection", e);
        }
    }
}