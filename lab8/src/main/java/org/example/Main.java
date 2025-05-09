package org.example;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        Connection connection = null;

        try {
            connection = Database.getConnection();

            // Disable auto-commit mode
            connection.setAutoCommit(false);

            ContinentDAO continentDAO = new ContinentDAO();
            continentDAO.create("Europe");

            // Commit after creating continent
            connection.commit();

            CountryDAO countryDAO = new CountryDAO();
            int europeId = continentDAO.findByName("Europe");
            countryDAO.addCountry("Romania", "RO", europeId);
            countryDAO.addCountry("Italy", "IT", europeId);

            // Commit after adding countries
            connection.commit();

            CityDAO cityDAO = new CityDAO();
            City city1 = new City(0, "Bucharest", "Romania",44.4268, 26.1025);
            City city2 = new City(0, "Rome", "Italy", 41.9028, 12.4964);

            cityDAO.addCity(city1);
            cityDAO.addCity(city2);

            // Commit the transaction after adding cities
            connection.commit();

            cityDAO.getAllCities();

            // Close connection when done
            connection.close();

        } catch (SQLException e) {
            System.err.println("Error: " + e);
            try {
                // Rollback the transaction in case of an error
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Error during rollback: " + ex);
            }
        }
    }
}
