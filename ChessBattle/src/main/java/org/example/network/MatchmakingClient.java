package org.example.network;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

public class MatchmakingClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Thread listenerThread;
    private static final int PORT = 5051;

    public void connect(String username, Consumer<String> onMessage) throws IOException {
        socket = new Socket("localhost", PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        in.readLine(); // WELCOME
        out.println(username);
        listenerThread = new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    onMessage.accept(line);
                }
            } catch (IOException e) {
                // Connection closed
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    public void sendChallenge(String targetUsername) {
        if (out != null) {
            out.println("CHALLENGE:" + targetUsername);
        }
    }

    public void sendCreateGameLobby() {
        if (out != null) {
            out.println("CREATE_GAME_LOBBY");
        }
    }

    public void sendJoinGameLobby(String gameId) {
        if (out != null) {
            out.println("JOIN_GAME_LOBBY:" + gameId);
        }
    }

    public void logout() {
        if (out != null) {
            out.println("LOGOUT");
        }
        try {
            if (socket != null) socket.close();
        } catch (IOException ignored) {}
    }
}
