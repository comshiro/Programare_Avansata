package org.example.lab11.controller;

import org.springframework.web.bind.annotation.*;
import org.example.lab11.CountryDAO;
import org.example.lab11.Country;
import java.util.List;

@RestController
@RequestMapping("/api/countries")
public class CountryController {

    private final CountryDAO countryDAO = new CountryDAO();

    @GetMapping
    public List<Country> getAllCountries() {
        return countryDAO.getAllCountries();
    }
}
