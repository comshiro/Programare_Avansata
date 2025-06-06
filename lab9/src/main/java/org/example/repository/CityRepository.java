package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.entity.City;
import java.util.List;

public class CityRepository {
    private final EntityManager em;
    public CityRepository(EntityManager em) { this.em = em; }

    public void create(City city) {
        em.getTransaction().begin();
        em.persist(city);
        em.getTransaction().commit();
    }

    public City findById(Integer id) {
        return em.find(City.class, id);
    }

    public List<City> findByName(String namePattern) {
        TypedQuery<City> query = em.createQuery("SELECT c FROM City c WHERE c.name LIKE :name", City.class);
        query.setParameter("name", namePattern);
        return query.getResultList();
    }
}
