package org.example.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "countries")
@NamedQueries({
    @NamedQuery(name = "Country.findAll", query = "select e from Country e order by e.name")
})
public class Country implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "continent_id", referencedColumnName = "id")
    private Continent continent;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<City> cities;

    public Country() {}
    public Country(String name, String code, Continent continent) {
        this.name = name;
        this.code = code;
        this.continent = continent;
    }
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public Continent getContinent() { return continent; }
    public void setContinent(Continent continent) { this.continent = continent; }
    public List<City> getCities() { return cities; }
    public void setCities(List<City> cities) { this.cities = cities; }
    @Override
    public String toString() { return "Country{" + "id=" + id + ", name='" + name + '\'' + ", code='" + code + '\'' + '}'; }
}
