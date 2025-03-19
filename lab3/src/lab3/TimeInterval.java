package lab3;
import java.time.LocalTime;

class TimeInterval extends Pair<LocalTime, LocalTime> {

    public TimeInterval(LocalTime start, LocalTime end) {
        super(start, end);
    }
}
