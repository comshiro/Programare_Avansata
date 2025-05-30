package org.example.lab11;

public class Country extends BaseModel {
    private String name;
    private String code;
    private int continentId;

    public Country(int id, String name, String code, int continentId) {
        this.setId(id);
        this.name = name;
        this.code = code;
        this.continentId = continentId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getContinentId() {
        return continentId;
    }

    public void setContinentId(int continentId) {
        this.continentId = continentId;
    }

    @Override
    public String toString() {
        return "Country{id=" + getId() + ", name='" + name + "', code='" + code + "', continentId=" + continentId + "}";
    }
}
