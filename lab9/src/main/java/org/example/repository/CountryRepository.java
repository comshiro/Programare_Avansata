package org.example.repository;

import org.example.entity.Country;
import java.util.List;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.EntityManager;

public class CountryRepository extends DataRepository<Country, Integer> {
    public CountryRepository() { super(); }

    public Country findById(Integer id) {
        return super.findById(Country.class, id).orElse(null);
    }

    public boolean create(Country country) {
        return super.persist(country) != null;
    }

    public List<Country> findByName(String name) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Country> query = em.createQuery("SELECT c FROM Country c WHERE c.name = :name", Country.class);
            query.setParameter("name", name);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
