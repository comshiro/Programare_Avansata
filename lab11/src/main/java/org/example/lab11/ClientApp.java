package org.example.lab11;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class ClientApp {
    private static final String BASE_URL = "http://localhost:8081/api/cities";
    private static final RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        // 1. Get all cities
        ResponseEntity<City[]> response = restTemplate.getForEntity(BASE_URL, City[].class);
        System.out.println("All cities:");
        Arrays.stream(response.getBody()).forEach(System.out::println);

        // 2. Add a new city
        City newCity = new City(0, "Romania", "Braila", false, 45.2692, 27.9575);
        City created = restTemplate.postForObject(BASE_URL, newCity, City.class);
        System.out.println("Added city: " + created);

        // 3. Update city name
        int cityId = created.getId();
        String updateUrl = BASE_URL + "/" + cityId + "/name";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = "{\"name\":\"Braila Noua\"}";
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<City> updated = restTemplate.exchange(updateUrl, HttpMethod.PUT, entity, City.class);
        System.out.println("Updated city: " + updated.getBody());

        // 4. Delete city
        restTemplate.delete(BASE_URL + "/" + cityId);
        System.out.println("Deleted city with id: " + cityId);
    }
}