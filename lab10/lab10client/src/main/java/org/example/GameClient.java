package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class GameClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 9090;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private final Scanner scanner;

    public GameClient() {
        scanner = new Scanner(System.in);
    }

    public void connect() throws IOException {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connected to server at " + SERVER_ADDRESS + ":" + SERVER_PORT);
        } catch (IOException e) {
            System.err.println("Failed to connect to server: " + e.getMessage());
            throw e;
        }
    }

    public void start() {
        try {
            // Start a thread to handle server responses
            new Thread(this::receiveMessages).start();

            // Main thread reads user input and sends to server
            String userInput;
            while (true) {
                System.out.print("Enter command: ");
                userInput = scanner.nextLine();

                // Send the command to the server
                out.println(userInput);

                // Check if user wants to exit
                if ("exit".equalsIgnoreCase(userInput)) {
                    System.out.println("Disconnecting from server...");
                    break;
                }
            }
        } finally {
            disconnect();
        }
    }

    private void receiveMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Server: " + message);
            }
        } catch (IOException e) {
            if (!socket.isClosed()) {
                System.err.println("Error reading from server: " + e.getMessage());
            }
        }
    }

    private void disconnect() {
        try {
            if (scanner != null) {
                scanner.close();
            }
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error while disconnecting: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        GameClient client = new GameClient();
        try {
            client.connect();
            client.start();
        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}