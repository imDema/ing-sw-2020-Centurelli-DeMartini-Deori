package it.polimi.ingsw.model.board;

public class Coordinate {
    int x, y;

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
}
