package org.example.ChessApp;

import javafx.application.Platform;

import java.io.*;
import java.net.Socket;

public class ChessClient {
    private final BoardController controller;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String playerId;

    public ChessClient(BoardController controller) {
        this.controller = controller;
    }

    public void connectToServer() {
        try {
            socket = new Socket("localhost", 5050);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            // Start listener thread
            new Thread(this::listenForServerMessages).start();
        } catch (IOException e) {
            controller.showMessage("Connection failed: " + e.getMessage());
        }
    }

    public void sendMessage(String message) {
        try {
            out.writeObject(message);
            out.flush();
            System.out.println("Sent: " + message);
        } catch (IOException e) {
            controller.showMessage("Error sending message: " + e.getMessage());
        }
    }

    private void listenForServerMessages() {
        try {
            while (true) {
                String message = (String) in.readObject();
                System.out.println("Received: " + message);
                handleServerMessage(message);
            }
        } catch (Exception e) {
            Platform.runLater(() -> controller.showMessage("Connection lost: " + e.getMessage()));
        }
    }

    private void handleServerMessage(String message) {
        String[] parts = message.split(":", 2);
        String command = parts[0];
        String data = parts.length > 1 ? parts[1] : "";

        Platform.runLater(() -> {
            switch (command) {
                case "CONNECTED":
                    playerId = data;
                    controller.showMessage("Connected as " + playerId);
                    break;

                case "GAME_CREATED":
                    controller.setGameId(data);
                    break;

                case "GAME_JOINED":
                    String[] joinData = data.split(":");
                    controller.setGameId(joinData[0]);
                    controller.setPlayerColor(joinData[1]);
                    break;

                case "BOARD_UPDATE":
                    controller.updateBoard(data);
                    break;

                case "TURN_UPDATE":
                    controller.updateTurn(data);
                    break;

                case "MOVE_VALID":
                    controller.showMessage("Move successful");
                    break;

                case "MOVE_INVALID":
                    controller.showMessage("Invalid move: " + data);
                    System.err.println("Server rejected move: " + data);
                    break;

                case "CHECK":
                    controller.showMessage("CHECK! " + data + " is in check");
                    break;

                case "GAME_OVER":
                    if (data.startsWith("SURRENDER:")) {
                        String surrenderPlayer = data.substring(9);
                        if (surrenderPlayer.equals(playerId)) {
                            controller.showMessage("You surrendered. Game over.");
                        } else {
                            controller.showMessage("Opponent surrendered. You win!");
                        }
                    } else {
                        controller.showMessage("Game over: " + data);
                    }
                    break;

                case "OPPONENT_DISCONNECTED":
                    controller.showMessage("Opponent disconnected. You win!");
                    break;

                case "ERROR":
                    controller.showMessage("Error: " + data);
                    break;

                default:
                    System.out.println("Unknown command: " + command);
                    break;
            }
        });
    }

    // Usage: sendMessage("CREATE_GAME_WITH_ID:" + gameId) to create a game with a specific ID
}