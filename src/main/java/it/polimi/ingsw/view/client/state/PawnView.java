package it.polimi.ingsw.view.client.state;

import it.polimi.ingsw.model.board.Coordinate;

public class PawnView {
    private final PlayerView owner;
    private final int id;
    private Coordinate position;

    public PlayerView getOwner() {
        return owner;
    }

    public String getSymbol() { return owner.getSymbol() + id;}

    public int getId() {
        return id;
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }

    public PawnView(PlayerView owner, int id) {
        this.owner = owner;
        this.id = id;
    }
}
