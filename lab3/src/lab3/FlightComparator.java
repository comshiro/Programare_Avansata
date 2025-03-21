package lab3;

import java.util.Comparator;

public class FlightComparator implements Comparator<Flight> {
    @Override
    public int compare(Flight f1, Flight f2) {
        return f1.getTimeInterval().getFirst().compareTo(f2.getTimeInterval().getFirst());
    }
}
