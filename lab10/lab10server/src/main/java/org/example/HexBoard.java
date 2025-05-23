package org.example;

public class HexBoard {
    public enum Cell { EMPTY, RED, BLUE }
    private final int size;
    private final Cell[][] board;

    // Union-Find for efficient win checking
    private final int[] parent;
    private final int[] rank;
    private final int redTop, redBottom, blueLeft, blueRight;

    public HexBoard(int size) {
        this.size = size;
        this.board = new Cell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = Cell.EMPTY;
            }
        }
        int n = size * size;
        parent = new int[n + 4]; // +4 for virtual nodes
        rank = new int[n + 4];
        for (int i = 0; i < n + 4; i++) parent[i] = i;
        redTop = n;
        redBottom = n + 1;
        blueLeft = n + 2;
        blueRight = n + 3;
    }

    private int idx(int row, int col) { return row * size + col; }

    private int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }
    private void union(int x, int y) {
        int xr = find(x), yr = find(y);
        if (xr == yr) return;
        if (rank[xr] < rank[yr]) parent[xr] = yr;
        else if (rank[xr] > rank[yr]) parent[yr] = xr;
        else { parent[yr] = xr; rank[xr]++; }
    }

    public int getSize() {
        return size;
    }

    public Cell getCell(int row, int col) {
        return board[row][col];
    }

    public boolean isValidMove(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size && board[row][col] == Cell.EMPTY;
    }

    public boolean placeStone(int row, int col, Cell color) {
        if (!isValidMove(row, col)) return false;
        board[row][col] = color;
        int pos = idx(row, col);
        // Union with neighbors of same color
        int[] dr = {-1, -1, 0, 0, 1, 1};
        int[] dc = {-1, 0, -1, 1, 0, 1};
        for (int d = 0; d < 6; d++) {
            int nr = row + dr[d], nc = col + dc[d];
            if (nr >= 0 && nr < size && nc >= 0 && nc < size && board[nr][nc] == color) {
                union(pos, idx(nr, nc));
            }
        }
        // Union with virtual nodes
        if (color == Cell.RED) {
            if (row == 0) union(pos, redTop);
            if (row == size - 1) union(pos, redBottom);
        } else if (color == Cell.BLUE) {
            if (col == 0) union(pos, blueLeft);
            if (col == size - 1) union(pos, blueRight);
        }
        return true;
    }

    // Win check for RED (top-bottom) and BLUE (left-right) using Union-Find
    public boolean checkWin(Cell color) {
        if (color == Cell.RED) {
            return find(redTop) == find(redBottom);
        } else if (color == Cell.BLUE) {
            return find(blueLeft) == find(blueRight);
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("   ");
        for (int j = 0; j < size; j++) {
            sb.append(j).append(" ");
        }
        sb.append("\n");
        for (int i = 0; i < size; i++) {
            sb.append(i < 10 ? " " : "").append(i).append(" ");
            for (int k = 0; k < i; k++) sb.append(" ");
            for (int j = 0; j < size; j++) {
                if (board[i][j] == Cell.EMPTY) sb.append(".");
                else if (board[i][j] == Cell.RED) sb.append("R");
                else sb.append("B");
                if (j < size - 1) sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
