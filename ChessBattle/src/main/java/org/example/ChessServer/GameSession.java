package org.example.ChessServer;

import java.util.ArrayList;
import java.util.List;

public class GameSession {
    private final String gameId;
    private String whitePlayer;
    private String blackPlayer;
    private int[][] board;
    private boolean whiteTurn = true;
    private final List<String> moveList = new ArrayList<>();
    private long startTime = System.currentTimeMillis();
    private long endTime = 0;

    public GameSession(String gameId, String whitePlayer) {
        this.gameId = gameId;
        this.whitePlayer = whitePlayer;
        initializeBoard();
    }

    private void initializeBoard() {
        board = new int[8][8];

        // White pieces: 1-6, Black pieces: 7-12
        // 1/7=pawn, 2/8=rook, 3/9=knight, 4/10=bishop, 5/11=queen, 6/12=king

        // Black pieces (top of board in server coordinates - rows 0-1)
        // Row 0: back rank
        board[0][0] = 8; board[0][1] = 9; board[0][2] = 10; board[0][3] = 11;
        board[0][4] = 12; board[0][5] = 10; board[0][6] = 9; board[0][7] = 8;
        // Row 1: pawns
        for (int i = 0; i < 8; i++) board[1][i] = 7;

        // White pieces (bottom of board in server coordinates - rows 6-7)
        // Row 6: pawns
        for (int i = 0; i < 8; i++) board[6][i] = 1;
        // Row 7: back rank
        board[7][0] = 2; board[7][1] = 3; board[7][2] = 4; board[7][3] = 5;
        board[7][4] = 6; board[7][5] = 4; board[7][6] = 3; board[7][7] = 2;

        // Print initial board for debugging
        System.out.println("Initial board state:");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public MoveResult makeMove(String playerId, int fromRow, int fromCol, int toRow, int toCol) {
        System.out.println("Processing move for player: " + playerId + " from (" + fromRow + "," + fromCol + ") to (" + toRow + "," + toCol + ")");
        System.out.println("White player: " + whitePlayer + ", Black player: " + blackPlayer);
        System.out.println("Current turn - white: " + whiteTurn);

        // Validate turn
        boolean isWhite = playerId.equals(whitePlayer);
        boolean isBlack = playerId.equals(blackPlayer);
        System.out.println("Player is white: " + isWhite + ", Player is black: " + isBlack);

        if ((whiteTurn && !isWhite) || (!whiteTurn && !isBlack)) {
            System.out.println("Not player's turn");
            return new MoveResult(false, "Not your turn", board);
        }

        int piece = board[fromRow][fromCol];
        System.out.println("Piece at source: " + piece);

        // Validate piece exists
        if (piece == 0) {
            System.out.println("No piece at selected position");
            return new MoveResult(false, "No piece at selected position", board);
        }

        // FIXED: White pieces are 1-6, Black pieces are 7-12
        boolean isWhitePiece = piece <= 6;
        System.out.println("Piece is white: " + isWhitePiece + " (piece value: " + piece + ")");

        if (isWhite != isWhitePiece) {
            System.out.println("Player trying to move opponent's piece");
            return new MoveResult(false, "You can only move your own pieces", board);
        }

        // Validate destination doesn't have own piece
        int targetPiece = board[toRow][toCol];
        if (targetPiece != 0) {
            boolean isTargetWhite = targetPiece <= 6;
            if (isWhite == isTargetWhite) {
                System.out.println("Cannot capture own piece");
                return new MoveResult(false, "Cannot capture your own piece", board);
            }
        }

        if (ChessEngine.isValidMove(board, fromRow, fromCol, toRow, toCol)) {
            // Save original state for rollback
            int originalFromPiece = board[fromRow][fromCol];
            int originalToPiece = board[toRow][toCol];

            // Make the move
            board[toRow][toCol] = originalFromPiece;
            board[fromRow][fromCol] = 0;

            // Check if move leaves own king in check
            boolean isPlayerWhite = playerId.equals(whitePlayer);
            if (ChessEngine.isCheck(board, isPlayerWhite)) {
                // Illegal move - revert
                board[fromRow][fromCol] = originalFromPiece;
                board[toRow][toCol] = originalToPiece;
                return new MoveResult(false, "Move would leave king in check", board);
            }

            // Record move in algebraic notation (simple: e2e4, or you can use a helper)
            String moveNotation = toAlgebraic(fromRow, fromCol, toRow, toCol, piece);
            moveList.add(moveNotation);

            // Switch turns
            whiteTurn = !whiteTurn;

            // Check for check/checkmate
            boolean opponentInCheck = ChessEngine.isCheck(board, isPlayerWhite);
            boolean opponentInCheckmate = opponentInCheck && ChessEngine.isCheckmate(board, isPlayerWhite);
            String winner = opponentInCheckmate ? (isPlayerWhite ? "WHITE" : "BLACK") : null;

            return new MoveResult(
                true, "Move successful", board,
                opponentInCheck, opponentInCheckmate, winner
            );
        }

        System.out.println("Move validation failed");
        return new MoveResult(false, "Invalid move for this piece", board);
    }

    private String toAlgebraic(int fromRow, int fromCol, int toRow, int toCol, int piece) {
        // Simple coordinate notation: e.g., e2e4
        return toSquare(fromRow, fromCol) + toSquare(toRow, toCol);
    }
    private String toSquare(int row, int col) {
        char file = (char)('a' + col);
        char rank = (char)('1' + (7 - row));
        return "" + file + rank;
    }

    private int[][] copyBoard(int[][] original) {
        int[][] copy = new int[8][8];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, 8);
        }
        return copy;
    }

    public int[][] getBoardState() {
        return board;
    }

    public String getWhitePlayer() {
        return whitePlayer;
    }

    public String getBlackPlayer() {
        return blackPlayer;
    }

    public void setBlackPlayer(String blackPlayer) {
        this.blackPlayer = blackPlayer;
    }

    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    public List<String> getMoveList() {
        return new ArrayList<>(moveList);
    }
    public int getDuration() {
        if (endTime == 0) {
            endTime = System.currentTimeMillis();
        }
        return (int)((endTime - startTime) / 1000); // duration in seconds
    }
    public void endGame() {
        endTime = System.currentTimeMillis();
    }
}