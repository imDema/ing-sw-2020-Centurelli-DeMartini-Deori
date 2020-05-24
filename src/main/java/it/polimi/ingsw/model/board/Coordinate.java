package it.polimi.ingsw.model.board;

import java.util.function.Function;

/**
 * The Coordinate class is the abstraction used to map the cells of the game board, each cell on the board
 * correspond to a coordinate;
 * A coordinate is determined by two fields: the row and the column of the board, these two fields
 * are internally represented by two int  values (x-> row and y->column)
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
     * @return true if this coordinate and c are adjacent
     */
    public boolean isNeighbour(Coordinate c) {
        return !(x > c.getX() + 1 || x < c.getX() - 1 || y > c.getY() + 1 || y < c.getY() - 1)
                && !(x == c.getX() && y == c.getY());
    }

    /**
     * @param function is applied to all the adjacent coordinates of the coordinate on which this method is called
     * @return a boolean flag that is true, if the function returns true on a adjacent coordinate of the one
     * on which the method is called, or false if the function never returns true on any of the adjacent coordinates
     * of the one on which it's called
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
