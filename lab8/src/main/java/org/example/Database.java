package org.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

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

    // closing the pool when the app ends
    public static void shutdown() {
        if (dataSource != null) {
            dataSource.close();
        }
    }
}
