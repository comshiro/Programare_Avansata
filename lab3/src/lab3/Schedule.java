package lab3;

import java.time.LocalTime;
import java.util.*;
import java.util.Comparator;

public class Schedule {

    private Airport airport;
    private Map<Flight, Runway> flightMap = new HashMap<>();
    private List<Runway> runways;

    public Schedule(Airport airport) {
        this.airport = airport;
        this.runways = airport.getRunways();
    }

    public void scheduleFlightsStartTime(List<Flight> flights) {
        Collections.sort(flights, new FlightComparator());

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

        return f1Start.isBefore(f2End) && f2Start.isBefore(f1End);
    }

    public Map<Flight, Runway> getFlightMap() {
        return flightMap;
    }

    public void scheduleFlightsEquitably(List<Flight> flights) {
        Collections.sort(flights, new FlightComparator());

        for (Flight flight : flights) {
            boolean assigned = false;

            for (Runway runway : LoadSort()) {
                boolean conflictFound = false;

                for (Flight scheduledFlight : runway.getScheduledFlights()) {
                    if (hasConflict(scheduledFlight, flight)) {
                        conflictFound = true;
                        break;
                    }
                }

                if (!conflictFound) {
                    runway.getScheduledFlights().add(flight);
                    assigned = true;
                    break;
                }
            }

            if (!assigned) {
                System.out.println("Flight " + flight.getId() + " could not be scheduled due to conflicts.");
            }
        }
        checkEquity();
    }

    public List<Runway> LoadSort() {
        Collections.sort(runways, new RunwayComparator());
        return runways;
    }
    public void checkEquity() {
        int minFlights = Integer.MAX_VALUE;
        int maxFlights = Integer.MIN_VALUE;
        int totalFlights = 0;

        for (Runway runway : runways) {
            int runwayFlightCount = runway.getScheduledFlights().size();
            totalFlights += runwayFlightCount;

            if (runwayFlightCount < minFlights) {
                minFlights = runwayFlightCount;
            }
            if (runwayFlightCount > maxFlights) {
                maxFlights = runwayFlightCount;
            }
        }

        if (maxFlights - minFlights > 1) {
            System.out.println("Equitable scheduling is not possible with the current number of runways.");

            int additionalRunwaysRequired = calculateAdditionalRunways(totalFlights, runways.size());
            System.out.println("Additional runways required: " + additionalRunwaysRequired);
        } else {
            System.out.println("Flights are scheduled equitably.");
        }
    }

    private int calculateAdditionalRunways(int totalFlights, int currentRunways) {
        int requiredRunways = totalFlights / (currentRunways + 1);
        return requiredRunways - currentRunways;
    }


    public void printRunwaySchedules() {
        for (Runway runway : runways) {
            System.out.println("Runway " + runway.getId() + ":");
            for (Flight flight : runway.getScheduledFlights()) {
                System.out.println("  Flight: Start=" + flight.getTimeInterval().getFirst() +
                        ", End=" + flight.getTimeInterval().getSecond());
            }
            System.out.println();
        }
    }



}