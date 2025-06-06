package org.example.model;

public class LeaderboardEntry {
    private String username;
    private int totalPoints;
    private int gamesPlayed;
    private int wins;
    private int losses;

    public LeaderboardEntry(String username, int totalPoints, int gamesPlayed, int wins, int losses) {
        this.username = username;
        this.totalPoints = totalPoints;
        this.gamesPlayed = gamesPlayed;
        this.wins = wins;
        this.losses = losses;
    }

    public String getUsername() { return username; }
    public int getTotalPoints() { return totalPoints; }
    public int getGamesPlayed() { return gamesPlayed; }
    public int getWins() { return wins; }
    public int getLosses() { return losses; }

    public void setUsername(String username) { this.username = username; }
    public void setTotalPoints(int totalPoints) { this.totalPoints = totalPoints; }
    public void setGamesPlayed(int gamesPlayed) { this.gamesPlayed = gamesPlayed; }
    public void setWins(int wins) { this.wins = wins; }
    public void setLosses(int losses) { this.losses = losses; }
}
