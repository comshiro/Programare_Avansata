/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab3;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author Administrator
 */
public class Flight implements Comparable<Flight> {
    private Aircraft aircraft;
    private Long id;
    private TimeInterval timeInterval;

    public Flight(Aircraft aircraft, Long id, TimeInterval timeInterval) {
        this.aircraft = aircraft;
        this.id = id;
        this.timeInterval = timeInterval;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TimeInterval getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(TimeInterval timeInterval) {
        this.timeInterval = timeInterval;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "aircraft=" + aircraft +
                ", id=" + id +
                ", timeInterval=" + timeInterval +
                '}';
    }

    @Override
    public int compareTo(Flight other) {
        return this.timeInterval.getFirst().compareTo(other.timeInterval.getFirst());
    }
}
