package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A thread that handles communication with a single client.
 * Processes incoming commands and returns appropriate responses.
 */
public class ClientThread implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(ClientThread.class.getName());

    private final Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    /**
     * Creates a new ClientThread to handle communication with the specified client socket.
     *
     * @param clientSocket The socket connected to the client
     */
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

                // Process the command
                String response = processCommand(inputLine);

                // Send response back to client
                out.println(response);

                // If the command was "stop", break the loop
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

    /**
     * Processes the command received from the client and returns an appropriate response.
     *
     * @param command The command to process
     * @return The response to send back to the client
     */
    private String processCommand(String command) {
        if (command.equalsIgnoreCase("stop")) {
            // Notify GameServer to shut down (implementation detail)
            shutdownServer();
            return "Server stopped";
        } else {
            // For any other command, just return a confirmation message
            return "Server received the request " + command;
        }
    }

    /**
     * Signal to the GameServer to shut down.
     * This method would be implemented based on how GameServer is designed to be stopped.
     */
    private void shutdownServer() {
        // This implementation depends on how the GameServer is designed to be stopped
        // Could be through a shared flag, a method call, etc.
        LOGGER.info("Server shutdown initiated by client");
    }

    /**
     * Closes all open resources associated with this client connection.
     */
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