import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.*;

public class PercolationTest {

    /**
     * Enum to represent the state of a cell in the grid. Use this enum to help you write tests.
     * <p>
     * (0) CLOSED: isOpen() returns true, isFull() return false
     * <p>
     * (1) OPEN: isOpen() returns true, isFull() returns false
     * <p>
     * (2) INVALID: isOpen() returns false, isFull() returns true
     *              (This should not happen! Only open cells should be full.)
     * <p>
     * (3) FULL: isOpen() returns true, isFull() returns true
     * <p>
     */
    private enum Cell {
        CLOSED, OPEN, INVALID, FULL
    }

    /**
     * Creates a Cell[][] based off of what Percolation p returns.
     * Use this method in your tests to see if isOpen and isFull are returning the
     * correct things.
     */
    private static Cell[][] getState(int N, Percolation p) {
        Cell[][] state = new Cell[N][N];
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                int open = p.isOpen(r, c) ? 1 : 0;
                int full = p.isFull(r, c) ? 2 : 0;
                state[r][c] = Cell.values()[open + full];
            }
        }
        return state;
    }

    @Test
    public void basicTest() {
        int N = 5;
        Percolation p = new Percolation(N);
        // open sites at (r, c) = (0, 1), (2, 0), (3, 1), etc. (0, 0) is top-left
        int[][] openSites = {
                {0, 1},
                {2, 0},
                {3, 1},
                {4, 1},
                {1, 0},
                {1, 1}
        };
        Cell[][] expectedState = {
                {Cell.CLOSED, Cell.FULL, Cell.CLOSED, Cell.CLOSED, Cell.CLOSED},
                {Cell.FULL, Cell.FULL, Cell.CLOSED, Cell.CLOSED, Cell.CLOSED},
                {Cell.FULL, Cell.CLOSED, Cell.CLOSED, Cell.CLOSED, Cell.CLOSED},
                {Cell.CLOSED, Cell.OPEN, Cell.CLOSED, Cell.CLOSED, Cell.CLOSED},
                {Cell.CLOSED, Cell.OPEN, Cell.CLOSED, Cell.CLOSED, Cell.CLOSED}
        };
        for (int[] site : openSites) {
            p.open(site[0], site[1]);
        }
        assertThat(getState(N, p)).isEqualTo(expectedState);
        assertThat(p.percolates()).isFalse();
    }

    @Test
    public void oneByOneTest() {
        int N = 1;
        Percolation p = new Percolation(N);
        p.open(0, 0);
        Cell[][] expectedState = {
                {Cell.FULL}
        };
        assertThat(getState(N, p)).isEqualTo(expectedState);
        assertThat(p.percolates()).isTrue();
    }

    @Test
    public void backwashTest() {
        int N = 4;
        Percolation p = new Percolation(N);
        p.open(0, 1);
        p.open(1, 1);
        p.open(2, 1);
        p.open(3, 1);
        p.open(3, 3);

        Cell[][] expectedState = {
                {Cell.CLOSED, Cell.FULL, Cell.CLOSED, Cell.CLOSED},
                {Cell.CLOSED, Cell.FULL, Cell.CLOSED, Cell.CLOSED},
                {Cell.CLOSED, Cell.FULL, Cell.CLOSED, Cell.CLOSED},
                {Cell.CLOSED, Cell.FULL, Cell.CLOSED, Cell.OPEN}
        };
        assertThat(getState(N, p)).isEqualTo(expectedState);  // Check for backwash
        assertTrue(p.percolates());
    }



    @Test
    public void cornerCaseTest() {
        int N = 2;
        Percolation p = new Percolation(N);
        p.open(0, 0);
        p.open(1, 1);

        assertFalse(p.isFull(1,1));
        assertFalse(p.percolates());

        p.open(1,0);
        assertTrue(p.percolates());
        assertTrue(p.isFull(1,0));
        assertTrue(p.isFull(1,1));

    }



    @Test
    void openInvalidIndicesTest() {
        int N = 5;
        Percolation p = new Percolation(N);
        assertThrows(IllegalArgumentException.class, () -> p.open(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> p.open(0, N));

    }
    @Test
    void isOpenInvalidIndicesTest() {
        int N = 5;
        Percolation p = new Percolation(N);
        assertThrows(IllegalArgumentException.class, () -> p.isOpen(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> p.isOpen(0, N));

    }

    @Test
    void isFullInvalidIndicesTest() {
        int N = 5;
        Percolation p = new Percolation(N);
        assertThrows(IllegalArgumentException.class, () -> p.isFull(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> p.isFull(0, N));

    }
}
