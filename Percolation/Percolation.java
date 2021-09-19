import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF uf;
    private boolean[][] grid;
    private final int size;
    private int openSite;
    public Percolation(int n) {
        uf = new WeightedQuickUnionUF(n*n + 2);
        size = n;
        openSite = 0;
        grid = new boolean[n+1][n+1];
        for (int i = 1; i <= n; i++) {
            for (int k = 1; k <= n; k++) {
                grid[i][k] = false;
            }
        }
    }

    public void open(int row, int col) {
        grid[row][col] = true;
        openSite++;
        if (row == 1) {
            uf.union((row-1)*size+col, 0);
        }
        else if (row == size) {
            uf.union((row-1)*size + col, size*size+1);
        }
        if (row > 1 && grid[row-1][col]) {
            uf.union((row-1)*size + col, (row-2)*size+col);
        }
        if (row < size && grid[row+1][col]) {
            uf.union((row-1)*size + col, row*size+col);
        }
        if (col > 1 && grid[row][col-1]) {
            uf.union((row-1)*size+col, (row-1)*size + (col-1));
        }
        if (col < size && grid[row][col+1]) {
            uf.union((row-1)*size + col, (row-1)*size + (col+1));
        }
    }

    public boolean isOpen(int row, int col) {
        if (grid[row][col]) return true;
        return false;
    }

    public boolean isFull(int row, int col) {
        if (uf.find((row-1)*size+col) == uf.find(0)) {
            return true;
        }
        return false;
    }

    public int numberOfOpenSites() {
        return openSite;
    }

    public boolean percolates() {
        return uf.find(0) == uf.find(size*size+1);
    }
}
