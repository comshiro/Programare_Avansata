package org.example.lab11;

public class Continent extends BaseModel {
    private String name;

    public Continent(int id, String name) {
        this.setId(id); //superclass method
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Continent{id=" + getId() + ", name='" + name + "'}";
    }
}
