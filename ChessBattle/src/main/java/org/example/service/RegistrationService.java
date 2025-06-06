package org.example.service;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Properties;

import org.example.model.Player;

import java.io.IOException;
import java.io.InputStream;

public class RegistrationService {
    private static final Properties config = new Properties();
    static {
        try (InputStream input = RegistrationService.class.getClassLoader().getResourceAsStream("config.properties")) {
            config.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Could not load config.properties", e);
        }
    }
    private static final String DB_URL = config.getProperty("db.url");
    private static final String DB_USER = config.getProperty("db.user");
    private static final String DB_PASS = config.getProperty("db.pass");

    public boolean registerPlayer(Player player, String plainPassword) throws SQLException, NoSuchAlgorithmException {
        String hashedPassword = hashPassword(plainPassword);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "INSERT INTO Players (username, email, password_hash) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, player.getUsername());
                stmt.setString(2, player.getEmail());
                stmt.setString(3, hashedPassword);
                stmt.executeUpdate();
                return true;
            } catch (SQLIntegrityConstraintViolationException e) {
                return false;
            }
        }
    }

    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
