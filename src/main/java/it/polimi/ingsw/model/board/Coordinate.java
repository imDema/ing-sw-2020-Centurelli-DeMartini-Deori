package it.polimi.ingsw.model.board;

public class Coordinate {
    private final int x, y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isNeighbour(Coordinate c2) {
        return !(x > c2.getX() + 1 || x < c2.getX() - 1 || y > c2.getY() + 1 || y < c2.getY() - 1)
                && !(x == c2.getX() && y == c2.getY());
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
