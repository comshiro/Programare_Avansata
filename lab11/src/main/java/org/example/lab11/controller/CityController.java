package org.example.lab11.controller;

import org.example.lab11.City;
import org.example.lab11.CityDAO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    private final CityDAO cityDAO = new CityDAO();

    @GetMapping
    public List<City> getAllCities() {
        try {
            return cityDAO.getAllCities();
        } catch (java.sql.SQLException e) {
            throw new RuntimeException("Failed to fetch cities", e);
        }
    }

    @PostMapping
    public City addCity(@RequestBody City city) {
        try {
            cityDAO.addCity(city);
            return city;
        } catch (java.sql.SQLException e) {
            throw new RuntimeException("Failed to add city", e);
        }
    }

    public static class NameRequest {
        public String name;
    }

    @PutMapping("/{id}/name")
    public City updateCityName(@PathVariable int id, @RequestBody NameRequest req) {
        try {
            cityDAO.updateCityName(id, req.name);
            return cityDAO.getCityById(id);
        } catch (java.sql.SQLException e) {
            throw new RuntimeException("Failed to update city name", e);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteCity(@PathVariable int id) {
        try {
            cityDAO.deleteCity(id);
        } catch (java.sql.SQLException e) {
            throw new RuntimeException("Failed to delete city", e);
        }
    }
}