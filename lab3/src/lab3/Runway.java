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
public class Runway implements Comparable<Runway> {
    private final String id;
    private final List<Flight> scheduledFlights;

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


    @Override
    public int compareTo(Runway other) {
        int thisLoad = this.scheduledFlights.size();
        int otherLoad = other.scheduledFlights.size();

        return Integer.compare(thisLoad, otherLoad);
    }
}
