package org.example;

public class HexAI {
    public int[] getMove(HexBoard board) {
        int size = board.getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board.getCell(i, j) == HexBoard.Cell.EMPTY) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    public int[] getMove(HexBoard board, HexBoard.Cell aiColor) {
        int size = board.getSize();
        HexBoard.Cell opponent = (aiColor == HexBoard.Cell.RED) ? HexBoard.Cell.BLUE : HexBoard.Cell.RED;
        // 1. Try to block opponent's potential path
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board.getCell(i, j) == HexBoard.Cell.EMPTY) {
                    // Simulate as opponent
                    board.placeStone(i, j, opponent);
                    boolean oppHasPath = hasPotentialPath(board, opponent);
                    board.placeStone(i, j, HexBoard.Cell.EMPTY); // undo
                    // Only block if after this move, opponent STILL has a path (original logic was inverted)
                    if (oppHasPath) {
                        // Actually play as AI's color
                        return new int[]{i, j};
                    }
                }
            }
        }
        // 2. Otherwise, pick a move that keeps AI's own potential path
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board.getCell(i, j) == HexBoard.Cell.EMPTY) {
                    board.placeStone(i, j, aiColor);
                    boolean aiHasPath = hasPotentialPath(board, aiColor);
                    board.placeStone(i, j, HexBoard.Cell.EMPTY); // undo
                    if (aiHasPath) {
                        return new int[]{i, j};
                    }
                }
            }
        }
        // 3. Fallback: pick first empty cell
        return getMove(board);
    }

    public boolean hasPotentialPath(HexBoard board, HexBoard.Cell color) {
        int size = board.getSize();
        boolean[][] visited = new boolean[size][size];
        if (color == HexBoard.Cell.RED) {
            for (int col = 0; col < size; col++) {
                if (dfsPotential(board, 0, col, color, visited)) return true;
            }
        } else if (color == HexBoard.Cell.BLUE) {
            for (int row = 0; row < size; row++) {
                if (dfsPotential(board, row, 0, color, visited)) return true;
            }
        }
        return false;
    }

    private boolean dfsPotential(HexBoard board, int row, int col, HexBoard.Cell color, boolean[][] visited) {
        int size = board.getSize();
        if (row < 0 || row >= size || col < 0 || col >= size) return false;
        if (visited[row][col]) return false;
        HexBoard.Cell cell = board.getCell(row, col);
        if (cell != HexBoard.Cell.EMPTY && cell != color) return false;
        visited[row][col] = true;
        if (color == HexBoard.Cell.RED && row == size - 1) return true;
        if (color == HexBoard.Cell.BLUE && col == size - 1) return true;
        int[] dr = {-1, -1, 0, 0, 1, 1};
        int[] dc = {-1, 0, -1, 1, 0, 1};
        for (int d = 0; d < 6; d++) {
            if (dfsPotential(board, row + dr[d], col + dc[d], color, visited)) return true;
        }
        return false;
    }
}
