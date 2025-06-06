package org.example;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        Connection connection = null;

        try {
            connection = Database.getConnection();

            // Create tables if they dont exist
            try {
                Database.createCitiesTableIfNotExists();
                Database.migrateCitiesTable();
            } catch (SQLException e) {
                System.err.println("Error creating or migrating cities table: " + e.getMessage());
            }

            // Disable auto-commit mode
            connection.setAutoCommit(false);

            ContinentDAO continentDAO = new ContinentDAO();
            continentDAO.create("Europe");
            connection.commit();

            CountryDAO countryDAO = new CountryDAO();
            int europeId = continentDAO.findByName("Europe");
            countryDAO.addCountry("Romania", "RO", europeId);
            countryDAO.addCountry("Italy", "IT", europeId);
            connection.commit();

            CityDAO cityDAO = new CityDAO();
            City city1 = new City(0, "Romania", "Bucharest", true, 44.4268, 26.1025);
            City city2 = new City(0, "Italy", "Rome", true, 41.9028, 12.4964);
            City city3 = new City(0, "France", "Paris", true, 48.8566, 2.3522);
            City city4 = new City(0, "USA", "New York", false, 40.7128, -74.0060);
            City city5 = new City(0, "Japan", "Tokyo", true, 35.6895, 139.6917);

            cityDAO.addCity(city1);
            cityDAO.addCity(city2);
            cityDAO.addCity(city3);
            cityDAO.addCity(city4);
            cityDAO.addCity(city5);
            connection.commit();

            // Example: Display all cities
            System.out.println("All cities:");
            for (City c : cityDAO.getAllCities()) {
                System.out.println(c.getId() + ": " + c.getName() + ", " + c.getCountry() + (c.isCapital() ? " (capital)" : ""));
            }

            System.out.println("\nDistances between cities:");
            double d1 = CityDAO.distance(city1.getLatitude(), city1.getLongitude(), city2.getLatitude(), city2.getLongitude());
            System.out.printf("%s - %s: %.2f km\n", city1.getName(), city2.getName(), d1);
            double d2 = CityDAO.distance(city1.getLatitude(), city1.getLongitude(), city3.getLatitude(), city3.getLongitude());
            System.out.printf("%s - %s: %.2f km\n", city1.getName(), city3.getName(), d2);
            double d3 = CityDAO.distance(city3.getLatitude(), city3.getLongitude(), city5.getLatitude(), city5.getLongitude());
            System.out.printf("%s - %s: %.2f km\n", city3.getName(), city5.getName(), d3);

            CityDAO.importCities("src/main/resources/concap.csv");

            connection.close();
        } catch (SQLException e) {
            System.err.println("Error: " + e);
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Error during rollback: " + ex);
            }
        } catch (Exception e) {
            System.err.println("General error: " + e);
        }
    }
}
