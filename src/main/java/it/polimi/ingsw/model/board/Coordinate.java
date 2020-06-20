package it.polimi.ingsw.model.board;

import java.util.function.Function;

/**
 * Two dimensional coordinate with {@link Integer} coefficients.
 */
public class Coordinate {
    private final int x, y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Check if a coordinate is neighbour
     * @param c Coordinate to check
     * @return true if this coordinate and c are adjacent or they are the same
     */
    public boolean isNeighbour(Coordinate c) {
        return !(x > c.getX() + 1 || x < c.getX() - 1 || y > c.getY() + 1 || y < c.getY() - 1);
    }

    /**
     * @param function is applied to all the adjacent coordinates of the coordinate on which this method is called
     * @return true if {@code function} evaluates to true for at least one neighbour coordinate
     */
    public boolean anyNeighbouring(Function<Coordinate, Boolean> function) {
        for (int i = Math.max(0, x-1); i < Math.min(Board.BOARD_SIZE, x+2); i++) {
            for (int j = Math.max(0, y-1); j < Math.min(Board.BOARD_SIZE, y+2); j++) {
                if (!(i == x && j == y) && function.apply(new Coordinate(i, j))) {
                    return true;
                }
            }
        }
        return false;
    }

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Coordinate) {
            Coordinate c = (Coordinate) obj;
            return c.x == x && c.y == y;
        }
        return false;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
