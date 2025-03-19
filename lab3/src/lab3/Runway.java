/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab3;
import java.util.List;
/**
 *
 * @author Naomi
 * mi s-a sugerat de IDE ca aceasta clasa sa fie record class
 *
 */
class Runway {
    private final String id;  // Unique identifier for the runway
    private final List<Flight> scheduledFlights; // List of assigned flights

    public Runway(String id, List<Flight> scheduledFlights) {
        this.id = id;
        this.scheduledFlights = scheduledFlights;
    }

    public String getId() {
        return id;
    }

    public List<Flight> getScheduledFlights() {
        return scheduledFlights;
    }

    @Override
    public String toString() {
        return "Runway{" +
                "id='" + id + '\'' +
                ", scheduledFlights=" + scheduledFlights +
                '}';
    }
}
