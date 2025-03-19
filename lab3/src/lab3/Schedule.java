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

    public boolean scheduleFlights(List<Flight> flights){

        
    }



}
