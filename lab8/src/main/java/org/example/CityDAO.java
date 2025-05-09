package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CityDAO {

    public void addCity(City city) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO cities (name, country, latitude, longitude) VALUES (?, ?, ?, ?)")) {

            ps.setString(1, city.getName());
            ps.setString(2, city.getCountry());
            ps.setDouble(3, city.getLatitude());
            ps.setDouble(4, city.getLongitude());

            ps.executeUpdate();
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
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude")
                );
                cities.add(city);
            }
        }
        return cities;
    }

    public void updateCity(City city) throws SQLException {
        String sql = "UPDATE cities SET country = ?, name = ?, latitude = ?, longitude = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, city.getCountry());
            ps.setString(2, city.getName());
            ps.setDouble(3, city.getLatitude());
            ps.setDouble(4, city.getLongitude());
            ps.setInt(5, city.getId());
            ps.executeUpdate();
        }
    }

    public void deleteCity(int id) throws SQLException {
        String sql = "DELETE FROM cities WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
