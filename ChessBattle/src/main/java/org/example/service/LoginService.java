package org.example.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Properties;

import org.example.model.Player;

import java.io.IOException;
import java.io.InputStream;

public class LoginService {
    private static final Properties config = new Properties();
    static {
        try (InputStream input = LoginService.class.getClassLoader().getResourceAsStream("config.properties")) {
            config.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Could not load config.properties", e);
        }
    }
    private static final String DB_URL = config.getProperty("db.url");
    private static final String DB_USER = config.getProperty("db.user");
    private static final String DB_PASS = config.getProperty("db.pass");

    public Player authenticate(String usernameOrEmail, String plainPassword) throws SQLException, NoSuchAlgorithmException {
        String hashedPassword = hashPassword(plainPassword);
        String sql = "SELECT id, username, email, password_hash, TO_CHAR(created_at, 'YYYY-MM-DD HH24:MI:SS') as created_at FROM Players WHERE (username = ? OR email = ?) AND password_hash = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usernameOrEmail);
            stmt.setString(2, usernameOrEmail);
            stmt.setString(3, hashedPassword);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Player(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getString("created_at")
                    );
                } else {
                    return null;
                }
            }
        }
    }

    private static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
