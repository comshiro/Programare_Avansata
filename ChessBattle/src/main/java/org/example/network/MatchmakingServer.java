package org.example.network;


import java.io.*;
import java.net.*;
import java.util.*;

public class MatchmakingServer {
    private static final int PORT = 5051;
    private static final Map<String, ClientHandler> onlineUsers = Collections.synchronizedMap(new HashMap<>());
    private static final Map<String, String> openGames = Collections.synchronizedMap(new LinkedHashMap<>()); // gameId -> creator

    public static void main(String[] args) {
        System.out.println("Matchmaking server started on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket;
        private String username;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                out.println("WELCOME");
                username = in.readLine(); // First message is username
                if (username == null || username.isEmpty()) {
                    socket.close();
                    return;
                }
                onlineUsers.put(username, this);
                broadcastOnlineUsers();
                broadcastOpenGames();
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("CHALLENGE:")) {
                        String target = line.substring(10);
                        handleChallenge(target);
                    } else if (line.equals("LOGOUT")) {
                        break;
                    } else if (line.equals("CREATE_GAME_LOBBY")) {
                        handleCreateGameLobby();
                    } else if (line.startsWith("JOIN_GAME_LOBBY:")) {
                        String gameId = line.substring(16);
                        handleJoinGameLobby(gameId);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (username != null) {
                    onlineUsers.remove(username);
                    // Remove any open games created by this user
                    openGames.entrySet().removeIf(entry -> entry.getValue().equals(username));
                    broadcastOpenGames();
                    broadcastOnlineUsers();
                }
                try { socket.close(); } catch (IOException ignored) {}
            }
        }

        private void handleChallenge(String target) {
            ClientHandler opponent = onlineUsers.get(target);
            if (opponent != null) {
                opponent.out.println("CHALLENGE_FROM:" + username);
            } else {
                out.println("USER_OFFLINE");
            }
        }

        private void handleCreateGameLobby() {
            // Generate a unique gameId
            String gameId = "GAME" + System.currentTimeMillis() % 10000;
            openGames.put(gameId, username);
            broadcastOpenGames();
        }

        private void handleJoinGameLobby(String gameId) {
            String creator = openGames.remove(gameId);
            if (creator != null) {
                // Notify both players to join the game
                ClientHandler creatorHandler = onlineUsers.get(creator);
                if (creatorHandler != null) {
                    creatorHandler.out.println("JOINED_GAME:" + gameId);
                }
                out.println("JOINED_GAME:" + gameId);
                broadcastOpenGames();
            } else {
                out.println("ERROR:Game not available");
            }
        }

        private void broadcastOnlineUsers() {
            String users = String.join(",", onlineUsers.keySet());
            for (ClientHandler handler : onlineUsers.values()) {
                handler.out.println("ONLINE_USERS:" + users);
            }
        }

        private void broadcastOpenGames() {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : openGames.entrySet()) {
                sb.append(entry.getKey()).append(" (by ").append(entry.getValue()).append(")").append(",");
            }
            String gamesList = sb.toString();
            for (ClientHandler handler : onlineUsers.values()) {
                handler.out.println("OPEN_GAMES:" + gamesList);
            }
        }
    }
}
