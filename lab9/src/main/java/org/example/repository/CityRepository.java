package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.example.entity.City;

import java.util.List;

public class CityRepository extends DataRepository<City, Integer> {
    public CityRepository() {
        super();
    }

    public City findById(Integer id) {
        return super.findById(City.class, id).orElse(null);
    }

    public boolean create(City city) {
        return super.persist(city) != null;
    }

    public List<City> findByName(String name) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<City> query = em.createNamedQuery("City.findByName", City.class);
            query.setParameter(1, name);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
