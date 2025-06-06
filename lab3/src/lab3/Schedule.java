package lab3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Schedule {

    private Airport airport;
    private Map<Flight, Runway> flightMap = new HashMap<>();
    private List<Runway> runways;

    public Schedule(Airport airport) {
        this.airport = airport;
        this.runways = airport.getRunways();
    }

    public void scheduleFlights() {
        // Initialize empty lists for each runway
        Map<Runway, List<Flight>> runwayFlights = new HashMap<>();
        for (Runway runway : runways) {
            runwayFlights.put(runway, new ArrayList<>());
        }

        for (Flight flight : airport.getFlights()) {
            boolean assigned = false;

            // Try to assign to an existing runway
            for (Runway runway : runways) {
                List<Flight> assignedFlights = runwayFlights.get(runway);
                if (!hasConflict(flight, assignedFlights)) {
                    assignedFlights.add(flight);
                    flightMap.put(flight, runway);
                    assigned = true;
                    break;
                }
            }


            if (!assigned)
                System.out.println("Warning: Flight " + flight + " could not be scheduled.");
        }
    }
}

private boolean hasConflict(Flight flight, List<Flight> runwayFlights) {
    for (Flight scheduledFlight : runwayFlights) {
        if (intervalsOverlap(scheduledFlight, flight)) {
            return true;
        }
    }
    return false;
}

private boolean intervalsOverlap(Flight f1, Flight f2) {
    return f1.getLandingStart().isBefore(f2.getLandingEnd()) &&
            f2.getLandingStart().isBefore(f1.getLandingEnd());
}

public Map<Flight, Runway> getFlightAssignments() {
    return flightMap;
}

public List<Flight> getFlightsForRunway(Runway runway) {
    List<Flight> flights = new ArrayList<>();
    for (Map.Entry<Flight, Runway> entry : flightMap.entrySet()) {
        if (entry.getValue().equals(runway)) {
            flights.add(entry.getKey());
        }
    }
    return flights;
}
}
