package org.example;

import java.sql.*;

public class ContinentDAO {

    public void create(String name) throws SQLException {
        if (findByName(name) == null) {
            try (Connection con = Database.getConnection();
                 PreparedStatement pstmt = con.prepareStatement("INSERT INTO continents (name) VALUES (?)")) {
                pstmt.setString(1, name);
                pstmt.executeUpdate();
                con.commit();
                System.out.println("Continent '" + name + "' added.");
            } catch (SQLException e) {
                System.err.println("Error inserting continent: " + e.getMessage());
            }
        } else {
            System.out.println("Continent '" + name + "' already exists.");
        }
    }

    public Integer findByName(String name) throws SQLException {
        String query = "SELECT id FROM continents WHERE name = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return null;
    }

    public String findById(int id) throws SQLException {
        String query = "SELECT name FROM continents WHERE id = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        }
        return null;
    }
}
