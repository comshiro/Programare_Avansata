package org.example;

import java.sql.*;
import org.example.factory.DaoFactory;

public class MainJPAOrJDBC {
    public static void main(String[] args) {
        String daoType = AppConfig.getDaoType();
        DaoFactory factory = DaoFactory.getFactory(daoType);

        Object continentRepo = factory.getContinentRepository();
        Object countryRepo = factory.getCountryRepository();
        Object cityRepo = factory.getCityRepository();

        if ("JPA".equalsIgnoreCase(daoType)) {
            // JPA logic
            org.example.repository.ContinentRepository continentDAO = (org.example.repository.ContinentRepository) continentRepo;
            org.example.repository.CountryRepository countryDAO = (org.example.repository.CountryRepository) countryRepo;
            org.example.repository.CityRepository cityDAO = (org.example.repository.CityRepository) cityRepo;

            // Create and persist continent
            continentDAO.create(new org.example.entity.Continent("Europe"));
            java.util.List<org.example.entity.Continent> continents = continentDAO.findByName("Europe");
            org.example.entity.Continent europe = continents.isEmpty() ? null : continents.get(0);
            if (europe != null) {
                // Create and persist countries
                org.example.entity.Country romania = new org.example.entity.Country("Romania", "RO", europe);
                org.example.entity.Country italy = new org.example.entity.Country("Italy", "IT", europe);
                countryDAO.create(romania);
                countryDAO.create(italy);

                // Create and persist cities (with population)
                org.example.entity.City city1 = new org.example.entity.City(romania, "Bucharest", true, 44.4268, 26.1025, 1800000);
                org.example.entity.City city2 = new org.example.entity.City(italy, "Rome", true, 41.9028, 12.4964, 2873000);
                org.example.entity.City city3 = new org.example.entity.City(italy, "Rimini", false, 44.0678, 12.5695, 150000);
                cityDAO.create(city1);
                cityDAO.create(city2);
                cityDAO.create(city3);

                // Display all cities
                System.out.println("All cities:");
                for (org.example.entity.City c : cityDAO.findAll(org.example.entity.City.class, "City.findAll")) {
                    System.out.println(c.getId() + ": " + c.getName() + ", " + c.getCountry().getName() + (c.isCapital() ? " (capital)" : "") + ", pop=" + c.getPopulation());
                }

                // Display distances
                System.out.println("\nDistances between cities:");
                double d1 = org.example.CityDAO.distance(city1.getLatitude(), city1.getLongitude(), city2.getLatitude(), city2.getLongitude());
                System.out.printf("%s - %s: %.2f km\n", city1.getName(), city2.getName(), d1);

                // --- Choco Solver Demo for JPA ---
                java.util.List<org.example.entity.City> allCities = cityDAO.findAll(org.example.entity.City.class, "City.findAll");
                int n = allCities.size();
                if (n > 0) {
                    int minPop = 1000000, maxPop = 5000000;
                    int[] populations = new int[n];
                    String[] names = new String[n];
                    String[] countries = new String[n];
                    for (int i = 0; i < n; i++) {
                        populations[i] = allCities.get(i).getPopulation();
                        names[i] = allCities.get(i).getName();
                        countries[i] = allCities.get(i).getCountry().getName();
                    }
                    org.chocosolver.solver.Model model = new org.chocosolver.solver.Model("City selection");
                    org.chocosolver.solver.variables.IntVar[] select = model.boolVarArray("select", n);
                    org.chocosolver.solver.variables.IntVar firstLetter = model.intVar('A', 'Z');
                    for (int i = 0; i < n; i++) {
                        model.ifThen(
                            model.arithm(select[i], "=", 1),
                            model.arithm(model.intVar(names[i].toUpperCase().charAt(0)), "=", firstLetter)
                        );
                    }
                    for (int i = 0; i < n; i++) {
                        for (int j = i + 1; j < n; j++) {
                            if (countries[i].equals(countries[j])) {
                                model.arithm(select[i], "+", select[j], "<=", 1).post();
                            }
                        }
                    }
                    org.chocosolver.solver.variables.IntVar totalPop = model.intVar(minPop, maxPop);
                    model.scalar(select, populations, "=", totalPop).post();
                    model.sum(select, ">=", 2).post();
                    org.chocosolver.solver.Solution solution = model.getSolver().findSolution();
                    if (solution != null) {
                        System.out.println("\nChoco: Cities with same starting letter, population in bounds, different countries:");
                        char letter = (char) solution.getIntVal(firstLetter);
                        int sum = 0;
                        for (int i = 0; i < n; i++) {
                            if (solution.getIntVal(select[i]) == 1) {
                                System.out.println(names[i] + " (" + countries[i] + ", pop=" + populations[i] + ")");
                                sum += populations[i];
                            }
                        }
                        System.out.println("Total population: " + sum + ", starting letter: " + letter);
                    } else {
                        System.out.println("No solution found for Choco city selection constraints.");
                    }
                }
            }
        } else {
            // JDBC logic (original)
            Connection connection = null;
            try {
                connection = Database.getConnection();
                Database.createCitiesTableIfNotExists();
                Database.migrateCitiesTable();
                connection.setAutoCommit(false);

                org.example.ContinentDAO continentDAO = (org.example.ContinentDAO) continentRepo;
                continentDAO.create("Europe");
                connection.commit();

                org.example.CountryDAO countryDAO = (org.example.CountryDAO) countryRepo;
                int europeId = continentDAO.findByName("Europe");
                countryDAO.addCountry("Romania", "RO", europeId);
                countryDAO.addCountry("Italy", "IT", europeId);
                connection.commit();

                org.example.CityDAO cityDAO = (org.example.CityDAO) cityRepo;
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

                System.out.println("All cities:");
                for (City c : cityDAO.getAllCities()) {
                    System.out.println(c.getId() + ": " + c.getName() + ", " + c.getCountry() + (c.isCapital() ? " (capital)" : ""));
                }

                System.out.println("\nDistances between cities:");
                double d1 = org.example.CityDAO.distance(city1.getLatitude(), city1.getLongitude(), city2.getLatitude(), city2.getLongitude());
                System.out.printf("%s - %s: %.2f km\n", city1.getName(), city2.getName(), d1);
                double d2 = org.example.CityDAO.distance(city1.getLatitude(), city1.getLongitude(), city3.getLatitude(), city3.getLongitude());
                System.out.printf("%s - %s: %.2f km\n", city1.getName(), city3.getName(), d2);
                double d3 = org.example.CityDAO.distance(city3.getLatitude(), city3.getLongitude(), city5.getLatitude(), city5.getLongitude());
                System.out.printf("%s - %s: %.2f km\n", city3.getName(), city5.getName(), d3);

                org.example.CityDAO.importCities("src/main/resources/concap.csv");
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
}
