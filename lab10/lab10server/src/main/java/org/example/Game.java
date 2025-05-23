package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Game {
    private final String gameId;
    private final List<Player> players;
    private final List<String> moves;
    private boolean started;
    private boolean finished;
    private int currentPlayerIndex;
    private final int initialTime;
    private long lastMoveTimestamp = -1;
    private HexBoard hexBoard;
    private HashMap<String, HexBoard.Cell> playerColors = new HashMap<>();
    private int boardSize = 7;

    public Game(String gameId, int initialTime) {
        this.gameId = gameId;
        this.players = new ArrayList<>();
        this.moves = new ArrayList<>();
        this.started = false;
        this.finished = false;
        this.currentPlayerIndex = 0;
        this.initialTime = initialTime;
        this.hexBoard = new HexBoard(boardSize);
    }

    public String getGameId() {
        return gameId;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isFinished() {
        return finished;
    }

    public HexBoard getHexBoard() {
        return hexBoard;
    }

    public void addPlayer(Player player) {
        if (players.size() < 2 && !started) {
            players.add(player);
            playerColors.put(player.getName(), players.size() == 1 ? HexBoard.Cell.RED : HexBoard.Cell.BLUE);
        }
    }

    public HexBoard.Cell getPlayerColor(String playerName) {
        return playerColors.get(playerName);
    }

    public boolean isPlayerTurn(String playerName) {
        return players.get(currentPlayerIndex).getName().equals(playerName);
    }

    public boolean makeHexMove(String playerName, int row, int col) {
        if (!isPlayerTurn(playerName) || finished) return false;
        HexBoard.Cell color = getPlayerColor(playerName);
        if (color == null) return false;
        boolean placed = hexBoard.placeStone(row, col, color);
        if (placed) {
            moves.add(playerName + " " + row + " " + col);
            if (hexBoard.checkWin(color)) {
                finished = true;
            } else {
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            }
        }
        return placed;
    }

    public void startGame() {
        if (players.size() == 2) {
            started = true;
            for (Player p : players) {
                p.setTimeLeft(initialTime);
            }
            lastMoveTimestamp = System.currentTimeMillis();
        }
    }

    public void submitMove(String move) {
        moves.add(move);
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void setFinished() {
        finished = true;
    }

    public int updateAndGetElapsedTime() {
        long now = System.currentTimeMillis();
        int elapsed = 0;
        if (lastMoveTimestamp > 0) {
            elapsed = (int) ((now - lastMoveTimestamp) / 1000);
        }
        lastMoveTimestamp = now;
        return elapsed;
    }
}
