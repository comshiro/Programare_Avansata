
package lab3;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Naomi
 */
public class Lab3 {

    public static void main(String[] args) {
        Drone d = new Drone(120L, 1000D, "AEX", 201L, "23K"); 
        Airliner a = new Airliner(1000L, 200L, 1000_000_000L, "324D", 342L, "LMAO");
        Freighter f = new Freighter(12043L, 20000000L, "DEF", 1234L, "ABC");
        
        Aircraft[] cargo = new Aircraft[2];
        cargo[0]=d;
        cargo[1]=f;
        System.out.println(cargo[0] + " "+ cargo[1]);

        Aircraft[] b = new Aircraft[3];
        b[0]=a;
        b[1]=d;
        b[2]=f;

        for(Aircraft aircraft: b)
            if(aircraft instanceof CargoCapable) {
                System.out.println(((CargoCapable)aircraft).toString());
            }

        Runway runway = new Runway("n", new ArrayList<>());
        Flight flight = new Flight(a, 2L, new TimeInterval(LocalTime.of(10, 0), LocalTime.of(11, 0)));
        runway.getScheduledFlights().add(flight);
        System.out.println("Scheduled Flights: " + runway.getScheduledFlights());

    Airport airport = new Airport("lkj", List.of(new Runway("r8", new ArrayList<>()), new Runway("r0", new ArrayList<>())));

        List<Flight> flights = new ArrayList<>(List.of(
                new Flight(new Airliner(12L, 212L, 34242, "ABC", 3213L, "defS"),
                        201L, new TimeInterval(LocalTime.of(11, 0), LocalTime.of(11, 30))),
                new Flight(new Freighter(20L, 505L, "CARGO101", 5000L, "ghi"),
                       321L , new TimeInterval(LocalTime.of(11, 15), LocalTime.of(11, 45))),
                new Flight(new Drone(30L, 707L, "DRONE-XYZ", 6L, "jkl"),
                        6554L, new TimeInterval(LocalTime.of(11, 40), LocalTime.of(12, 10)))));

        Schedule schedule = new Schedule(airport);
        schedule.scheduleFlightsStartTime(flights);

        //System.out.println(schedule.getFlightMap());
        schedule.printRunwaySchedule();
    }
    
}
