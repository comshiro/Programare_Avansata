package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenericDAO<T extends BaseModel> {

    private final Class<T> type;

    public GenericDAO(Class<T> type) {
        this.type = type;
    }

    // Create method: Inserts a new record into the table
    public void create(T model, String query) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            // Set the model fields here, e.g., stmt.setString(1, model.getName());
            // Example for Continent:
            stmt.setString(1, model.getName());
            stmt.executeUpdate();

            // Get generated key (ID)
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    model.setId(rs.getInt(1)); // Set the generated ID to the model
                }
            }
        }
    }

    // Find all records
    public List<T> findAll(String query) throws SQLException, IllegalAccessException, InstantiationException {
        List<T> result = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                T model = type.newInstance(); // Instantiate the model dynamically
                model.setId(rs.getInt("id")); // Set the ID
                // Set other fields here, e.g., model.setName(rs.getString("name"));
                result.add(model);
            }
        }
        return result;
    }

    // Find by ID
    public T findById(int id, String query) throws SQLException, IllegalAccessException, InstantiationException {
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    T model = type.newInstance(); // Instantiate the model
                    model.setId(rs.getInt("id"));
                    // Set other fields here
                    return model;
                }
            }
        }
        return null;
    }

    // Find by Name
    public T findByName(String name, String query) throws SQLException, IllegalAccessException, InstantiationException {
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    T model = type.newInstance(); // Instantiate the model
                    model.setId(rs.getInt("id"));
                    // Set other fields here
                    return model;
                }
            }
        }
        return null;
    }
}
