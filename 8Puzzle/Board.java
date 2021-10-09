import edu.princeton.cs.algs4.Queue;

public class Board {
    private final int n;
    private int[][] board;
    private int emptyRow;
    private int emptyCol;

    public Board(int[][] blocks) {
        n = blocks.length;
        board = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] == 0) {
                    emptyRow = i;
                    emptyCol = j;
                }
                board[i][j] = blocks[i][j];
            }
        }
    }

    public int dimension() {
        return n;
    }

    public int hamming() {
        int block;
        int total = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                block = board[i][j];
                if (block != 0)
                    total += hammingDistance(block, i, j);
            }
        }
        return total;
    }

    public int manhattan() {
        int block;
        int total = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                block = board[i][j];
                if (block != 0)
                    total += manhattanDistance(block, i, j);
            }
        }
        return total;
    }

    public boolean isGoal() {
        return this.hamming() == 0;
    }

    public Board twin() {
        int tmp;
        Board twinBoard = new Board(this.copy());
        if (board[0][0] == 0 || board[0][1] == 0) {
            twinBoard.swap(1, 0, 1, 1);
        } else {
            twinBoard.swap(0, 0, 0, 1);
        }
        return twinBoard;
    }

    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (that.n != this.n) return false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (that.board[i][j] != this.board[i][j])
                    return false;
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        Queue<Board> q = new Queue<Board>();
        Board copy;
        if (emptyRow > 0) {
            copy = new Board(this.copy());
            copy.swap(emptyRow, emptyCol, emptyRow-1, emptyCol);
            q.enqueue(copy);
        }
        if (emptyRow < n-1) {
            copy = new Board(this.copy());
            copy.swap(emptyRow, emptyCol, emptyRow+1, emptyCol);
            q.enqueue(copy);
        }
        if (emptyCol > 0) {
            copy = new Board(this.copy());
            copy.swap(emptyRow, emptyCol, emptyRow, emptyCol-1);
            q.enqueue(copy);
        }
        if (emptyCol < n-1) {
            copy = new Board(this.copy());
            copy.swap(emptyRow, emptyCol, emptyRow, emptyCol+1);
            q.enqueue(copy);
        }
        return q;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", board[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    private int hammingDistance(int block, int i, int j) {
        int goalRow = (block - 1) / n;
        int goalCol = (block - 1) % n;
        if (goalRow == i && goalCol == j)
            return 0;
        return 1;
    }

    private int manhattanDistance(int block, int i, int j) {
        int goalRow = (block - 1) / n;
        int goalCol = (block - 1) % n;
        return Math.abs(goalRow - i) + Math.abs(goalCol - j);
    }

    private int[][] copy() {
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                copy[i][j] = board[i][j];
        return copy;
    }

    private void swap(int r1, int c1, int r2, int c2) {
        int tmp = board[r1][c1];
        board[r1][c1] = board[r2][c2];
        board[r2][c2] = tmp;
        if (board[r1][c1] == 0) {
            emptyRow = r1;
            emptyCol = c1;
        }
        if (board[r2][c2] == 0) {
            emptyRow = r2;
            emptyCol = c2;
        }

    }
}