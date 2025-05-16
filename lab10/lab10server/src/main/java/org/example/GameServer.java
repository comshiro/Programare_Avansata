package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameServer {
    private static final Logger LOGGER = Logger.getLogger(GameServer.class.getName());

    private final int port;
    private ServerSocket serverSocket;
    private boolean running;
    private final ExecutorService threadPool;

    /**
     * Creates a new GameServer that will listen on the specified port.
     *
     * @param port The port number to listen on
     */
    public GameServer(int port) {
        this.port = port;
        this.threadPool = Executors.newCachedThreadPool();
    }

    /**
     * Starts the server, making it listen for client connections.
     */
    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            running = true;
            LOGGER.info("Game server started on port " + port);

            while (running) {
                try {
                    // Wait for a client to connect
                    Socket clientSocket = serverSocket.accept();
                    LOGGER.info("Client connected: " + clientSocket.getInetAddress());

                    // Create a new thread to handle the client
                    ClientThread clientThread = new ClientThread(clientSocket);
                    threadPool.execute(clientThread);

                } catch (IOException e) {
                    if (running) {
                        LOGGER.log(Level.SEVERE, "Error accepting client connection", e);
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error starting server on port " + port, e);
        } finally {
            shutdown();
        }
    }

    /**
     * Stops the server and releases all resources.
     */
    public void stop() {
        running = false;
        shutdown();
    }

    private void shutdown() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                LOGGER.info("Server socket closed");
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Error closing server socket", e);
            }
        }

        threadPool.shutdown();
        LOGGER.info("Game server shutdown complete");
    }

    /**
     * Main method to start the GameServer.
     */
    public static void main(String[] args) {
        int port = 9090; // Default port

        // Use command line argument for port if provided
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                LOGGER.warning("Invalid port number. Using default port " + port);
            }
        }

        GameServer server = new GameServer(port);
        server.start();
    }
}

