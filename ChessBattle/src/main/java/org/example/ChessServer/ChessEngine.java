package org.example.ChessServer;

public class ChessEngine {
    public static boolean isCheck(int[][] board, boolean isWhite) {
        int king = isWhite ? 6 : 12;
        int kingRow = -1, kingCol = -1;

        // Find the king
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board[row][col] == king) {
                    kingRow = row;
                    kingCol = col;
                    break;
                }
            }
        }

        if (kingRow == -1) return false; // King not found

        // Check if any opponent piece can attack the king
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                int piece = board[row][col];
                if (piece != 0 && (piece <= 6) != isWhite) {
                    if (isValidMove(board, row, col, kingRow, kingCol)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isCheckmate(int[][] board, boolean isWhite) {
        if (!isCheck(board, isWhite)) return false;

        // Try all possible moves to see if any escape check
        for (int fromRow = 0; fromRow < 8; fromRow++) {
            for (int fromCol = 0; fromCol < 8; fromCol++) {
                int piece = board[fromRow][fromCol];
                if (piece == 0 || (piece <= 6) != isWhite) continue;

                for (int toRow = 0; toRow < 8; toRow++) {
                    for (int toCol = 0; toCol < 8; toCol++) {
                        if (isValidMove(board, fromRow, fromCol, toRow, toCol)) {
                            // Make a test move
                            int originalPiece = board[toRow][toCol];
                            board[toRow][toCol] = board[fromRow][fromCol];
                            board[fromRow][fromCol] = 0;

                            boolean stillInCheck = isCheck(board, isWhite);

                            // Undo move
                            board[fromRow][fromCol] = board[toRow][toCol];
                            board[toRow][toCol] = originalPiece;

                            if (!stillInCheck) {
                                return false; // Found escape move
                            }
                        }
                    }
                }
            }
        }
        return true; // No escape moves found
    }

    public static boolean isValidMove(int[][] board, int fromRow, int fromCol, int toRow, int toCol) {
        int piece = board[fromRow][fromCol];
        if (piece == 0) return false;

        // White pieces: 1-6, Black pieces: 7-12
        if (piece == 1 || piece == 7) { // Pawn (white/black)
            return validatePawnMove(board, fromRow, fromCol, toRow, toCol, piece <= 6);
        } else if (piece == 2 || piece == 8) { // Rook (white/black)
            return validateRookMove(board, fromRow, fromCol, toRow, toCol);
        } else if (piece == 3 || piece == 9) { // Knight (white/black)
            return validateKnightMove(fromRow, fromCol, toRow, toCol);
        } else if (piece == 4 || piece == 10) { // Bishop (white/black)
            return validateBishopMove(board, fromRow, fromCol, toRow, toCol);
        } else if (piece == 5 || piece == 11) { // Queen (white/black)
            return validateQueenMove(board, fromRow, fromCol, toRow, toCol);
        } else if (piece == 6 || piece == 12) { // King (white/black)
            return validateKingMove(fromRow, fromCol, toRow, toCol);
        } else {
            return false;
        }
    }

    private static boolean validatePawnMove(int[][] board, int fromRow, int fromCol,
                                            int toRow, int toCol, boolean isWhite) {
        // In server coordinates: White starts at row 6, moves to row 5,4,3... (decreasing)
        // Black starts at row 1, moves to row 2,3,4... (increasing)
        int direction = isWhite ? -1 : 1;  // White moves up (decreasing row), black moves down (increasing row)

        // Move forward
        if (fromCol == toCol) {
            // Single move
            if (toRow == fromRow + direction && board[toRow][toCol] == 0) {
                return true;
            }
            // Double move from starting position
            if (isWhite && fromRow == 6 && toRow == 4 &&
                    board[5][fromCol] == 0 && board[4][fromCol] == 0) {
                return true;
            }
            if (!isWhite && fromRow == 1 && toRow == 3 &&
                    board[2][fromCol] == 0 && board[3][fromCol] == 0) {
                return true;
            }
        }

        // Capture diagonally
        if (Math.abs(toCol - fromCol) == 1 && toRow == fromRow + direction) {
            int targetPiece = board[toRow][toCol];
            if (targetPiece != 0 && isWhite != (targetPiece <= 6)) {
                return true;
            }
        }

        return false;
    }

    private static boolean validateRookMove(int[][] board, int fromRow, int fromCol, int toRow, int toCol) {
        if (fromRow != toRow && fromCol != toCol) return false;

        // Horizontal move
        if (fromRow == toRow) {
            int step = fromCol < toCol ? 1 : -1;
            for (int col = fromCol + step; col != toCol; col += step) {
                if (board[fromRow][col] != 0) return false;
            }
        }
        // Vertical move
        else {
            int step = fromRow < toRow ? 1 : -1;
            for (int row = fromRow + step; row != toRow; row += step) {
                if (board[row][fromCol] != 0) return false;
            }
        }

        return true;
    }

    private static boolean validateKnightMove(int fromRow, int fromCol, int toRow, int toCol) {
        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);
        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
    }

    private static boolean validateBishopMove(int[][] board, int fromRow, int fromCol, int toRow, int toCol) {
        if (Math.abs(toRow - fromRow) != Math.abs(toCol - fromCol)) return false;

        int rowStep = toRow > fromRow ? 1 : -1;
        int colStep = toCol > fromCol ? 1 : -1;

        int steps = Math.abs(toRow - fromRow);
        for (int i = 1; i < steps; i++) {
            int row = fromRow + i * rowStep;
            int col = fromCol + i * colStep;
            if (board[row][col] != 0) return false;
        }

        return true;
    }

    private static boolean validateQueenMove(int[][] board, int fromRow, int fromCol, int toRow, int toCol) {
        return validateRookMove(board, fromRow, fromCol, toRow, toCol) ||
                validateBishopMove(board, fromRow, fromCol, toRow, toCol);
    }

    private static boolean validateKingMove(int fromRow, int fromCol, int toRow, int toCol) {
        return Math.abs(toRow - fromRow) <= 1 && Math.abs(toCol - fromCol) <= 1;
    }
}