import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;

public class Percolation {
    private final boolean[][] grid;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF ufFull;
    private final int size;
    private int openSites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Grid size must be positive.");
        }
        size = n;
        grid = new boolean[n][n];
        uf = new WeightedQuickUnionUF(n * n + 2); // +2 for virtual top and bottom
        ufFull = new WeightedQuickUnionUF(n * n + 1); // +1 for virtual top
        openSites = 0;
    }

    // converts (row, col) to 1D index
    private int xyToIndex(int row, int col) {
        return (row - 1) * size + (col - 1);
    }

    // checks if (row, col) is within the grid
    private boolean isValid(int row, int col) {
        return row >= 1 && row <= size && col >= 1 && col <= size;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isValid(row, col)) {
            throw new IllegalArgumentException("Invalid row or column.");
        }
        if (!isOpen(row, col)) {
            grid[row - 1][col - 1] = true;
            openSites++;

            // connect to virtual top if it's the top row
            if (row == 1) {
                uf.union(xyToIndex(row, col), size * size);
                ufFull.union(xyToIndex(row, col), size * size);
            }

            // connect to virtual bottom if it's the bottom row
            if (row == size) {
                uf.union(xyToIndex(row, col), size * size + 1);
            }

            // connect to adjacent open sites
            if (row > 1 && isOpen(row - 1, col)) {
                uf.union(xyToIndex(row, col), xyToIndex(row - 1, col));
                ufFull.union(xyToIndex(row, col), xyToIndex(row - 1, col));
            }
            if (row < size && isOpen(row + 1, col)) {
                uf.union(xyToIndex(row, col), xyToIndex(row + 1, col));
                ufFull.union(xyToIndex(row, col), xyToIndex(row + 1, col));
            }
            if (col > 1 && isOpen(row, col - 1)) {
                uf.union(xyToIndex(row, col), xyToIndex(row, col - 1));
                ufFull.union(xyToIndex(row, col), xyToIndex(row, col - 1));
            }
            if (col < size && isOpen(row, col + 1)) {
                uf.union(xyToIndex(row, col), xyToIndex(row, col + 1));
                ufFull.union(xyToIndex(row, col), xyToIndex(row, col + 1));
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!isValid(row, col)) {
            throw new IllegalArgumentException("Invalid row or column.");
        }
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!isValid(row, col)) {
            throw new IllegalArgumentException("Invalid row or column.");
        }
        return isOpen(row, col) && ufFull.find(xyToIndex(row, col)) ==
                ufFull.find(size * size);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(size * size) == uf.find(size * size + 1);
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = 20;
        int trials = 1000;

        Percolation perc = new Percolation(n);
        System.out.println("Initially percolates? " + perc.percolates());

        perc.open(1, 1);
        System.out.println("After opening (1, 1):");
        System.out.println("  Is (1, 1) full? " + perc.isFull(1, 1));
        System.out.println("  Percolates? " + perc.percolates());

        double[] thresholds = new double[trials];
        for (int i = 0; i < trials; i++) {
            thresholds[i] = monteCarloSimulation(n);
        }

        double meanThreshold = StdStats.mean(thresholds);
        double stdDev = StdStats.stddev(thresholds);
        System.out.println("\nPercolation Threshold Statistics:");
        System.out.println("  Mean: " + meanThreshold);
        System.out.println("  Standard Deviation: " + stdDev);
    }

    private static double monteCarloSimulation(int n) {
        Percolation perc = new Percolation(n);
        while (!perc.percolates()) {
            int row = StdRandom.uniformInt(1, n + 1);
            int col = StdRandom.uniformInt(1, n + 1);
            perc.open(row, col);
        }
        return (double) perc.numberOfOpenSites() / (n * n);
    }
}