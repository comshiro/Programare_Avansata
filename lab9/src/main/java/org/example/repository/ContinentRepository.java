package org.example.repository;

import org.example.entity.Continent;
import java.util.List;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.EntityManager;

public class ContinentRepository extends DataRepository<Continent, Integer> {
    public ContinentRepository() { super(); }

    public Continent findById(Integer id) {
        return super.findById(Continent.class, id).orElse(null);
    }

    public boolean create(Continent continent) {
        return super.persist(continent) != null;
    }

    public List<Continent> findByName(String name) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Continent> query = em.createQuery("SELECT c FROM Continent c WHERE c.name = :name", Continent.class);
            query.setParameter("name", name);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
