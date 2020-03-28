package ingsw2020.group8.santorini.model.board;

import java.lang.UnsupportedOperationException;


public class Coordinate {
    int x, y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isNeighbour(Coordinate c2) {
        throw new UnsupportedOperationException();
    }

    public Coordinate (int x, int y) {
        this.x = x;
        this.y = y;
    }
}
