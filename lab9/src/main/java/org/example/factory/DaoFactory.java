package org.example.factory;

public abstract class DaoFactory {
    public abstract Object getCityRepository();
    public abstract Object getCountryRepository();
    public abstract Object getContinentRepository();

    public static DaoFactory getFactory(String type) {
        if ("JPA".equalsIgnoreCase(type)) {
            return new JpaDaoFactory();
        } else {
            return new JdbcDaoFactory();
        }
    }
}
