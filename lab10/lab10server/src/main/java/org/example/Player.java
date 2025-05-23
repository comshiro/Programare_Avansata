package org.example;

public class Player {
    private final String name;
    private int timeLeft; // in seconds

    public Player(String name, int timeLeft) {
        this.name = name;
        this.timeLeft = timeLeft;
    }

    public String getName() {
        return name;
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public void decrementTime(int seconds) {
        this.timeLeft -= seconds;
        if (this.timeLeft < 0) this.timeLeft = 0;
    }
}
