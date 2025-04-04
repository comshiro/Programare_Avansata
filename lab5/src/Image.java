import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record Image  (String name, LocalDate date, String path) implements Serializable{
    public Image(String name, LocalDate date, String path) {
        this.name = name;
        this.date = date;
        this.path = path;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public LocalDate date() {
        return date;
    }

    @Override
    public String path() {
        return path;
    }

    @Override
    public String toString() {
        return "Name: " + name + " | Date: " + date + " | Path: " + path;
    }
}