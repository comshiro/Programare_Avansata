package lab3;

import java.time.LocalTime;
import java.util.*;

public class Schedule {

    private Airport airport;
    private Map<Flight, Runway> flightMap = new HashMap<>();
    private List<Runway> runways;

    public Schedule(Airport airport) {
        this.airport = airport;
        this.runways = airport.getRunways();
    }

    public void scheduleFlightsStartTime(List<Flight> flights) {
        //Collections.sort(flights);
        flights.sort(new FlightComparator());

        for (Flight flight : flights) {
            boolean assigned = false;

            for (Runway runway : runways) {
                if (canAssignRunway(runway, flight)) {
                    flightMap.put(flight, runway);
                    runway.getScheduledFlights().add(flight);
                    assigned = true;
                    break;
                }
            }

            if (!assigned) {
                System.out.println("No available runway for flight " + flight.getId());
            }
        }
    }

    private boolean canAssignRunway(Runway runway, Flight flight) {
        for (Flight scheduledFlight : runway.getScheduledFlights()) {
            if (hasConflict(scheduledFlight, flight)) {
                return false;
            }
        }
        return true;
    }
    private boolean hasConflict(Flight f1, Flight f2) {
        LocalTime f1Start = f1.getTimeInterval().getFirst();
        LocalTime f1End = f1.getTimeInterval().getSecond();
        LocalTime f2Start = f2.getTimeInterval().getFirst();
        LocalTime f2End = f2.getTimeInterval().getSecond();

        return f1Start.isBefore(f2End) && f2Start.isBefore(f1End); // Overlapping time intervals
    }

    public Map<Flight, Runway> getFlightMap() {
        return flightMap;
    }
}