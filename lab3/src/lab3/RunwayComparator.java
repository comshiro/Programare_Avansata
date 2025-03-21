package lab3;

import java.util.Comparator;

public class RunwayComparator implements Comparator<Runway> {

    @Override
    public int compare(Runway runway1, Runway runway2) {

        int load1 = runway1.getScheduledFlights().size();
        int load2 = runway2.getScheduledFlights().size();


        return Integer.compare(load1, load2);
    }
}

