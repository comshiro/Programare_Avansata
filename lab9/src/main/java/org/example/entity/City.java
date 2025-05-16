package org.example.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cities")
@NamedQueries({
    @NamedQuery(name = "City.findAll", query = "select e from City e order by e.name"),
    @NamedQuery(name = "City.findByCountry", query = "select e from City e where e.country = ?1")
})
public class City implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    private Country country;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "capital", nullable = false)
    private boolean capital;

    @Column(name = "latitude", nullable = false)
    private double latitude;

    @Column(name = "longitude", nullable = false)
    private double longitude;

    @Column(name = "population", nullable = false)
    private int population;

    public City() {}
    public City(Country country, String name, boolean capital, double latitude, double longitude, int population) {
        this.country = country;
        this.name = name;
        this.capital = capital;
        this.latitude = latitude;
        this.longitude = longitude;
        this.population = population;
    }
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Country getCountry() { return country; }
    public void setCountry(Country country) { this.country = country; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public boolean isCapital() { return capital; }
    public void setCapital(boolean capital) { this.capital = capital; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public int getPopulation() { return population; }
    public void setPopulation(int population) { this.population = population; }
    @Override
    public String toString() { return "City{" + "id=" + id + ", name='" + name + '\'' + ", country='" + (country != null ? country.getName() : null) + '\'' + ", population=" + population + '}'; }
}
