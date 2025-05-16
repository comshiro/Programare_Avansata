package org.example.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "continents")
@NamedQueries({
    @NamedQuery(name = "Continent.findAll", query = "select e from Continent e order by e.name")
})
public class Continent implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "continent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Country> countries;

    public Continent() {}
    public Continent(String name) { this.name = name; }
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Country> getCountries() { return countries; }
    public void setCountries(List<Country> countries) { this.countries = countries; }
    @Override
    public String toString() { return "Continent{" + "id=" + id + ", name='" + name + '\'' + '}'; }
}
