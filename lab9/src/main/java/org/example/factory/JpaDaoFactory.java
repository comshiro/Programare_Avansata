package org.example.factory;

import org.example.repository.CityRepository;
import org.example.repository.CountryRepository;
import org.example.repository.ContinentRepository;

public class JpaDaoFactory extends DaoFactory {
    @Override
    public CityRepository getCityRepository() {
        return new CityRepository();
    }
    @Override
    public CountryRepository getCountryRepository() {
        return new CountryRepository();
    }
    @Override
    public ContinentRepository getContinentRepository() {
        return new ContinentRepository();
    }
}
