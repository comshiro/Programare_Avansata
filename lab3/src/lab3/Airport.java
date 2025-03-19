/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab3;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Naomi
 */
public class Airport {
    private String name;
    private List<Runway> runways = new ArrayList<>();

    public Airport(String name, List<Runway> runways) {
        this.name = name;
        this.runways = runways;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Runway> getRunways() {
        return runways;
    }
    public void setRunways(List<Runway> runways) {
        this.runways = runways;
    }

    @Override
    public String toString() {
        return "Airport{" +
                "name='" + name + '\'' +
                ", runways=" + runways +
                '}';
    }
}
