package ingsw2020.group8.santorini.model.board;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Coordinate {
    int x, y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isNeighbour(Coordinate c2) {
        throw new NotImplementedException();
    }

    public Coordinate (int x, int y) {
        this.x = x;
        this.y = y;
    }
}
