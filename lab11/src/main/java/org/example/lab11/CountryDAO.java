package org.example.lab11;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CountryDAO {

    public void addCountry(String name, String code, int continentId) {
        // Check if the country already exists
        if (findCountryByName(name) != null) {
            System.out.println("Country '" + name + "' already exists.");
            return;
        }

        String sql = "INSERT INTO countries (name, code, continent_id) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, code);
            ps.setInt(3, continentId);
            int rowsAffected = ps.executeUpdate();

            System.out.println("Rows affected: " + rowsAffected);

            conn.commit();  // Optional if you're managing commits manually
        } catch (SQLException e) {
            System.err.println("SQL Exception during country insertion: " + e.getMessage());
        }
    }

    public Country findCountryById(int id) {
        Country country = null;
        String sql = "SELECT * FROM countries WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                country = new Country(rs.getInt("id"), rs.getString("name"), rs.getString("code"), rs.getInt("continent_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return country;
    }

    public Country findCountryByName(String name) {
        Country country = null;
        String sql = "SELECT * FROM countries WHERE name = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                country = new Country(rs.getInt("id"), rs.getString("name"), rs.getString("code"), rs.getInt("continent_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return country;
    }

    public List<Country> getAllCountries() {
        List<Country> countries = new ArrayList<>();
        String sql = "SELECT * FROM countries";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                countries.add(new Country(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("code"),
                    rs.getInt("continent_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countries;
    }
}
