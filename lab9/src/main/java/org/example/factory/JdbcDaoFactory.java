package org.example.factory;

public class JdbcDaoFactory extends DaoFactory {
    @Override
    public Object getCityRepository() {
        return new org.example.CityDAO();
    }
    @Override
    public Object getCountryRepository() {
        return new org.example.CountryDAO();
    }
    @Override
    public Object getContinentRepository() {
        return new org.example.ContinentDAO();
    }
}
