package org.example.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;

public class LobbyService {
    private static final Properties config = new Properties();
    static {
        try (InputStream input = LobbyService.class.getClassLoader().getResourceAsStream("config.properties")) {
            config.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Could not load config.properties", e);
        }
    }
    private static final String DB_URL = config.getProperty("db.url");
    private static final String DB_USER = config.getProperty("db.user");
    private static final String DB_PASS = config.getProperty("db.pass");

    public ObservableList<String> getAvailablePlayers(String currentUsername) {
        ObservableList<String> playerList = FXCollections.observableArrayList();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(
                "SELECT username FROM Players WHERE username <> ?")) {
            stmt.setString(1, currentUsername);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    playerList.add(rs.getString("username"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playerList;
    }
}
