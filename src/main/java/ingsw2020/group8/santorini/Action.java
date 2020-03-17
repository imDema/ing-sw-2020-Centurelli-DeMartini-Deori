package ingsw2020.group8.santorini;

public abstract class Action {
    private Coordinate position;
    private Pawn pawn;

    public abstract boolean execute(Board board);
}
