package org.example.service;

import java.sql.*;
import java.util.List;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;
import org.example.model.GameHistory;
import org.example.model.LeaderboardEntry;
import java.util.ArrayList;

public class GameService {
    private static final Properties config = new Properties();
    static {
        try (InputStream input = GameService.class.getClassLoader().getResourceAsStream("config.properties")) {
            config.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Could not load config.properties", e);
        }
    }
    private static final String DB_URL = config.getProperty("db.url");
    private static final String DB_USER = config.getProperty("db.user");
    private static final String DB_PASS = config.getProperty("db.pass");

    public void saveGame(String player1, String player2, String result, int duration, String moves) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            int p1Id = getPlayerId(conn, player1);
            int p2Id = getPlayerId(conn, player2);
            // Store moves as a single string (comma-separated)
            CallableStatement stmt = conn.prepareCall("{call saveGame(?, ?, ?, ?, ?)}");
            stmt.setInt(1, p1Id);
            stmt.setInt(2, p2Id);
            stmt.setString(3, result);
            stmt.setInt(4, duration);
            stmt.setString(5, moves); // Store as VARCHAR or CLOB
            stmt.execute();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getPlayerId(Connection conn, String username) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT id FROM Players WHERE username = ?")) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("Player not found: " + username);
                }
            }
        }
    }

    public String getBestOpponent(String username) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            int playerId = getPlayerId(conn, username);
            CallableStatement stmt = conn.prepareCall("{? = call getBestOpponent(?)}");
            stmt.registerOutParameter(1, Types.INTEGER);
            stmt.setInt(2, playerId);
            stmt.execute();
            int opponentId = stmt.getInt(1);
            stmt.close();
            if (opponentId == 0) return null;
            // Fetch username for opponentId
            try (PreparedStatement ps = conn.prepareStatement("SELECT username FROM Players WHERE id = ?")) {
                ps.setInt(1, opponentId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getString("username");
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<GameHistory> getGameHistoryList(String username) {
        List<GameHistory> historyList = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            int playerId = getPlayerId(conn, username);
            // Try to use the new PL/SQL procedure with moves if available
            try (CallableStatement stmt = conn.prepareCall("{call getGameHistory(?, ?)}")) {
                stmt.setInt(1, playerId);
                stmt.registerOutParameter(2, Types.REF_CURSOR);
                stmt.execute();
                ResultSet rs = (ResultSet) stmt.getObject(2);
                while (rs.next()) {
                    historyList.add(new GameHistory(
                        rs.getInt("id"),
                        rs.getString("player1"),
                        rs.getString("player2"),
                        rs.getString("result"),
                        rs.getInt("duration"),
                        rs.getString("moves")
                    ));
                }
                rs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return historyList;
    }

    // Optionally keep the old string version for quick display
    public String getGameHistory(String username) {
        List<GameHistory> historyList = getGameHistoryList(username);
        StringBuilder sb = new StringBuilder();
        for (GameHistory gh : historyList) {
            sb.append(gh.toString()).append("\n");
        }
        return sb.toString();
    }

    public List<LeaderboardEntry> getLeaderboard() {
        List<LeaderboardEntry> leaderboard = new ArrayList<>();
        String sql = "SELECT p.username, l.total_points, l.games_played, l.wins, l.losses " +
                     "FROM Leaderboard l JOIN Players p ON l.player_id = p.id " +
                     "ORDER BY l.total_points DESC, l.wins DESC, l.games_played ASC";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                leaderboard.add(new LeaderboardEntry(
                    rs.getString("username"),
                    rs.getInt("total_points"),
                    rs.getInt("games_played"),
                    rs.getInt("wins"),
                    rs.getInt("losses")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return leaderboard;
    }

    public static String getDbUrl() { return DB_URL; }
    public static String getDbUser() { return DB_USER; }
    public static String getDbPass() { return DB_PASS; }
}
