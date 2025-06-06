package org.example.ChessServer;

public class MoveResult {
    private final boolean valid;
    private final String message;
    private final int[][] board;
    private final boolean check;
    private final boolean checkmate;
    private final String winner;
    private final boolean draw;
    private final boolean stalemate;

    public MoveResult(boolean valid, String message, int[][] board) {
        this(valid, message, board, false, false, null);
    }

    public MoveResult(boolean valid, String message, int[][] board,
                      boolean check, boolean checkmate, String winner) {
        this(valid, message, board, check, checkmate, winner, false, false);
    }
    public MoveResult(boolean valid, String message, int[][] board,
                      boolean check, boolean checkmate, String winner,
                      boolean draw, boolean stalemate) {
        this.valid = valid;
        this.message = message;
        this.board = board;
        this.check = check;
        this.checkmate = checkmate;
        this.winner = winner;
        this.draw = draw;
        this.stalemate = stalemate;
    }

    public boolean isValid() { return valid; }
    public String getMessage() { return message; }
    public int[][] getBoard() { return board; }
    public boolean isCheck() { return check; }
    public boolean isCheckmate() { return checkmate; }
    public String getWinner() { return winner; }
    public boolean isDraw() { return draw; }
    public boolean isStalemate() { return stalemate; }
    public String getCurrentPlayer() { return winner == null ? null : winner.equals("WHITE") ? "BLACK" : "WHITE"; }
}