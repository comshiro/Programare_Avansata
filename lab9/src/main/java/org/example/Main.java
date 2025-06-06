package org.example;

import jakarta.persistence.EntityManager;
import org.example.JPAUtil;
import org.example.entity.City;
import org.example.entity.Country;
import org.example.entity.Continent;
import org.example.repository.CityRepository;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        CityRepository cityRepo = new CityRepository(em);

        // Create and persist a continent
        Continent europe = new Continent();
        europe.setName("Europe");
        em.persist(europe);

        // Create and persist a country
        Country romania = new Country();
        romania.setName("Romania");
        romania.setCode("RO");
        romania.setContinent(europe);
        em.persist(romania);

        // Create and persist a city
        City bucharest = new City();
        bucharest.setName("Bucharest");
        bucharest.setCountry(romania);
        bucharest.setCapital(true);
        bucharest.setLatitude(44.4268);
        bucharest.setLongitude(26.1025);
        bucharest.setPopulation(1800000);
        cityRepo.create(bucharest);

        // Find by id
        City found = cityRepo.findById(bucharest.getId());
        System.out.println("Found by id: " + found);

        // Find by name pattern
        List<City> cities = cityRepo.findByName("Buch%");
        System.out.println("Found by name pattern (Buch%):");
        for (City c : cities) {
            System.out.println(c);
        }

        em.close();
        JPAUtil.close();
    }
}
