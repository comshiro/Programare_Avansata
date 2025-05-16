package org.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class Database {
    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:E:/PA/lab8/src/main/resources/world_cities.db");
        config.setUsername("");  // SQLite doesn't need a username
        config.setPassword("");  // SQLite doesn't need a password
        config.setMaximumPoolSize(10);

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        // Get connection from the pool
        Connection conn = dataSource.getConnection();

        conn.setAutoCommit(false);

        return conn;
    }

    public static void closeConnection(Connection conn) throws SQLException {
        // For connection pooling, it's better to return the connection to the pool instead of closing it.
        if (conn != null && !conn.isClosed()) {
            conn.close(); // Closing will return the connection to the pool
        }
    }

    public static void createCitiesTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS cities (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "country TEXT NOT NULL," +
                "name TEXT NOT NULL," +
                "capital BOOLEAN NOT NULL," +
                "latitude REAL NOT NULL," +
                "longitude REAL NOT NULL" +
                ")";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            conn.commit();
        }
    }

    public static void migrateCitiesTable() throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("PRAGMA table_info(cities)");
            boolean hasCapital = false;
            while (rs.next()) {
                if ("capital".equalsIgnoreCase(rs.getString("name"))) {
                    hasCapital = true;
                    break;
                }
            }
            if (!hasCapital) {
                stmt.executeUpdate("ALTER TABLE cities ADD COLUMN capital BOOLEAN NOT NULL DEFAULT 0");
                conn.commit();
                System.out.println("Added missing 'capital' column to 'cities' table.");
            }
        }
    }

    // closing the pool when the app ends
    public static void shutdown() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
