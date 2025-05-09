package org.example;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            var continents = new ContinentDAO();
            continents.create("Europe");
            Database.getConnection().commit();  // Commit after creating continent

            var countries = new CountryDAO();
            int europeId = continents.findByName("Europe");
            countries.addCountry("Romania", "RO", europeId);
            countries.addCountry("Italy", "IT", europeId);
            Database.getConnection().commit();  // Commit after adding countries

            Database.getConnection().close();  // Close connection when done

        } catch (SQLException e) {
            System.err.println(e);
        }
    }
}
