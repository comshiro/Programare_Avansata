package org.example.model;

public class GameHistory {
    private int gameId;
    private String player1;
    private String player2;
    private String result;
    private int duration;
    private String moves; // comma-separated move list

    public GameHistory(int gameId, String player1, String player2, String result, int duration, String moves) {
        this.gameId = gameId;
        this.player1 = player1;
        this.player2 = player2;
        this.result = result;
        this.duration = duration;
        this.moves = moves;
    }

    public int getGameId() { return gameId; }
    public String getPlayer1() { return player1; }
    public String getPlayer2() { return player2; }
    public String getResult() { return result; }
    public int getDuration() { return duration; }
    public String getMoves() { return moves; }

    public void setGameId(int gameId) { this.gameId = gameId; }
    public void setPlayer1(String player1) { this.player1 = player1; }
    public void setPlayer2(String player2) { this.player2 = player2; }
    public void setResult(String result) { this.result = result; }
    public void setDuration(int duration) { this.duration = duration; }
    public void setMoves(String moves) { this.moves = moves; }

    @Override
    public String toString() {
        return "Game #" + gameId + ": " + player1 + " vs " + player2 + ", Result: " + result + ", Duration: " + duration + " moves, Moves: " + moves;
    }
}
