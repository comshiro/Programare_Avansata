package org.example.ChessServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChessServer {
    private static final int PORT = 5050;
    private static GameManager gameManager = new GameManager();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Chess Server running on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected");
                new Thread(new ClientHandler(clientSocket, gameManager)).start();
            }
        } catch (IOException e) {
            System.err.println("Server exception: " + e.getMessage());
        }
    }
}