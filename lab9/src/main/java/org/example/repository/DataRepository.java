package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.io.Serializable;
import java.util.Optional;
import java.util.List;
import jakarta.persistence.TypedQuery;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.util.logging.Level;

public abstract class DataRepository<T, ID extends Serializable> {
    protected final EntityManagerFactory emf = org.example.JPAUtil.getEntityManagerFactory();
    private static final Logger logger = Logger.getLogger(DataRepository.class.getName());
    static {
        try {
            FileHandler fh = new FileHandler("jpa.log", true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            logger.setUseParentHandlers(true); // log to console and file
        } catch (Exception e) {
            System.err.println("Failed to set up logger: " + e);
        }
    }

    protected EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public T persist(T entity) {
        EntityManager em = getEntityManager();
        long start = System.currentTimeMillis();
        try {
            em.getTransaction().begin();
            T merged = em.merge(entity);
            em.getTransaction().commit();
            long end = System.currentTimeMillis();
            logger.info("Persisted entity " + entity.getClass().getSimpleName() + " in " + (end - start) + " ms");
            return merged;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Exception during persist: " + ex, ex);
            throw ex;
        } finally {
            em.close();
        }
    }

    public Optional<T> findById(Class<T> clazz, ID id) {
        EntityManager em = getEntityManager();
        long start = System.currentTimeMillis();
        try {
            Optional<T> result = Optional.ofNullable(em.find(clazz, id));
            long end = System.currentTimeMillis();
            logger.info("findById(" + clazz.getSimpleName() + ", " + id + ") executed in " + (end - start) + " ms");
            return result;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Exception during findById: " + ex, ex);
            throw ex;
        } finally {
            em.close();
        }
    }

    public List<T> findAll(Class<T> clazz, String namedQuery) {
        EntityManager em = getEntityManager();
        long start = System.currentTimeMillis();
        try {
            TypedQuery<T> query = em.createNamedQuery(namedQuery, clazz);
            List<T> result = query.getResultList();
            long end = System.currentTimeMillis();
            logger.info("findAll(" + clazz.getSimpleName() + ", '" + namedQuery + "') executed in " + (end - start) + " ms");
            return result;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Exception during findAll: " + ex, ex);
            throw ex;
        } finally {
            em.close();
        }
    }

    public void deleteById(Class<T> clazz, ID id) {
        EntityManager em = getEntityManager();
        long start = System.currentTimeMillis();
        try {
            em.getTransaction().begin();
            T entity = em.find(clazz, id);
            if (entity != null) {
                em.remove(entity);
            }
            em.getTransaction().commit();
            long end = System.currentTimeMillis();
            logger.info("deleteById(" + clazz.getSimpleName() + ", " + id + ") executed in " + (end - start) + " ms");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Exception during deleteById: " + ex, ex);
            throw ex;
        } finally {
            em.close();
        }
    }

    public List<T> findByName(Class<T> clazz, String namedQuery, String name) {
        EntityManager em = getEntityManager();
        long start = System.currentTimeMillis();
        try {
            TypedQuery<T> query = em.createNamedQuery(namedQuery, clazz);
            query.setParameter(1, name);
            List<T> result = query.getResultList();
            long end = System.currentTimeMillis();
            logger.info("findByName(" + clazz.getSimpleName() + ", '" + name + "') executed in " + (end - start) + " ms");
            return result;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Exception during findByName: " + ex, ex);
            throw ex;
        } finally {
            em.close();
        }
    }
}
