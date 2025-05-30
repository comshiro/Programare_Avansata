package org.example.lab11;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;

public class CityDAO {

    public boolean cityExists(String name, String country) throws SQLException {
        String sql = "SELECT 1 FROM cities WHERE name = ? AND country = ? LIMIT 1";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, country);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    public void addCity(City city) throws SQLException {
        if (cityExists(city.getName(), city.getCountry())) {
            System.out.println("City '" + city.getName() + "' in '" + city.getCountry() + "' already exists.");
            return;
        }
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO cities (name, country, capital, latitude, longitude) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, city.getName());
            ps.setString(2, city.getCountry());
            ps.setBoolean(3, city.isCapital());
            ps.setDouble(4, city.getLatitude());
            ps.setDouble(5, city.getLongitude());

            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                city.setId(keys.getInt(1));
            }
            conn.commit(); // Ensure the insert is committed
        }
    }

    public City getCityById(int id) throws SQLException {
        City city = null;
        String sql = "SELECT * FROM cities WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                city = new City(
                        rs.getInt("id"),
                        rs.getString("country"),
                        rs.getString("name"),
                        rs.getBoolean("capital"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude")
                );
            }
        }
        return city;
    }

    public List<City> getAllCities() throws SQLException {
        List<City> cities = new ArrayList<>();
        String sql = "SELECT * FROM cities";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                City city = new City(
                        rs.getInt("id"),
                        rs.getString("country"),
                        rs.getString("name"),
                        rs.getBoolean("capital"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude")
                );
                cities.add(city);
            }
        }
        return cities;
    }

    public void updateCity(City city) throws SQLException {
        String sql = "UPDATE cities SET country = ?, name = ?, capital = ?, latitude = ?, longitude = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, city.getCountry());
            ps.setString(2, city.getName());
            ps.setBoolean(3, city.isCapital());
            ps.setDouble(4, city.getLatitude());
            ps.setDouble(5, city.getLongitude());
            ps.setInt(6, city.getId());
            ps.executeUpdate();
        }
    }

    public void updateCityName(int id, String newName) throws SQLException {
        String sql = "UPDATE cities SET name = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newName);
            ps.setInt(2, id);
            ps.executeUpdate();
            conn.commit();
        }
    }

    public void deleteCity(int id) throws SQLException {
        String sql = "DELETE FROM cities WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            conn.commit(); 
        }
    }

    public static void importCities(String csvFilePath) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            CityDAO cityDAO = new CityDAO();
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) { isFirstLine = false; continue; } // skip header
                String[] tokens = line.split(",");
                if (tokens.length < 6) continue;
                int id = 0; // ignore CSV id, let DB autoincrement
                String country = tokens[1];
                String name = tokens[2];
                boolean capital = Boolean.parseBoolean(tokens[3]);
                String latStr = tokens[4].trim();//hande null
                String lonStr = tokens[5].trim();
                // Skip if latitude or longitude is not a valid number
                try {
                    if (latStr.equalsIgnoreCase("NULL") || latStr.isEmpty() || lonStr.equalsIgnoreCase("NULL") || lonStr.isEmpty()) continue;
                    double latitude = Double.parseDouble(latStr);
                    double longitude = Double.parseDouble(lonStr);
                    City city = new City(id, country, name, capital, latitude, longitude);
                    cityDAO.addCity(city);
                } catch (NumberFormatException e) {
                    // Skip this row if lat/lon is not a valid number (e.g., 'GS', 'N/A', etc.)
                    continue;
                }
            }
        }
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon1 - lon2);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
