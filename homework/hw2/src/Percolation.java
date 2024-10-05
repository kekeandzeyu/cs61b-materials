import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;

public class Percolation {
    private final boolean[][] grid;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF ufFull;
    private final int size;
    private int openSites;

    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("Grid size must be positive.");
        }
        size = N;
        grid = new boolean[N][N];
        uf = new WeightedQuickUnionUF(N * N + 2); // +2 for virtual top and bottom
        ufFull = new WeightedQuickUnionUF(N * N + 1); // +1 for virtual top
        openSites = 0;
    }

    public void open(int row, int col) {
        if (!isValid(row, col)) {
            throw new IllegalArgumentException("Invalid row or column.");
        }
        if (!isOpen(row, col)) {
            grid[row][col] = true;
            openSites++;

            // connect to virtual top if it's the top row
            if (row == 0) {
                uf.union(xyToIndex(row, col), size * size);
                ufFull.union(xyToIndex(row, col), size * size);
            }

            // connect to virtual bottom if it's the bottom row
            if (row == size - 1) {
                uf.union(xyToIndex(row, col), size * size + 1);
            }

            // connect to adjacent open sites
            if (row > 0 && isOpen(row - 1, col)) {
                uf.union(xyToIndex(row, col), xyToIndex(row - 1, col));
                ufFull.union(xyToIndex(row, col), xyToIndex(row - 1, col));
            }
            if (row < size - 1 && isOpen(row + 1, col)) {
                uf.union(xyToIndex(row, col), xyToIndex(row + 1, col));
                ufFull.union(xyToIndex(row, col), xyToIndex(row + 1, col));
            }
            if (col > 0 && isOpen(row, col - 1)) {
                uf.union(xyToIndex(row, col), xyToIndex(row, col - 1));
                ufFull.union(xyToIndex(row, col), xyToIndex(row, col - 1));
            }
            if (col < size - 1 && isOpen(row, col + 1)) {
                uf.union(xyToIndex(row, col), xyToIndex(row, col + 1));
                ufFull.union(xyToIndex(row, col), xyToIndex(row, col + 1));
            }
        }
    }

    public boolean isOpen(int row, int col) {
        if (!isValid(row, col)) {
            throw new IllegalArgumentException("Invalid row or column.");
        }
        return grid[row][col];
    }

    public boolean isFull(int row, int col) {
        if (!isValid(row, col)) {
            throw new IllegalArgumentException("Invalid row or column.");
        }
        return isOpen(row, col) && ufFull.find(xyToIndex(row, col)) ==
                ufFull.find(size * size);
    }

    public int numberOfOpenSites() {
        return openSites;
    }

    public boolean percolates() {
        return uf.find(size * size) == uf.find(size * size + 1);
    }

    private boolean isValid(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    private int xyToIndex(int row, int col) {
        return row * size + col;
    }

    private static double monteCarloSimulation(int N) {
        Percolation perc = new Percolation(N);
        while (!perc.percolates()) {
            int row = StdRandom.uniform(N);
            int col = StdRandom.uniform(N);
            perc.open(row, col);
        }
        return (double) perc.numberOfOpenSites() / (N * N);
    }
}
