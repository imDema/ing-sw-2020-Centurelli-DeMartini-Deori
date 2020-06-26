package it.polimi.ingsw.view.client.controls;

import it.polimi.ingsw.model.board.Coordinate;

/**
 * Client side pawn state representation
 */
public class PawnViewState {
    private final PlayerViewState owner;
    private final int id;
    private Coordinate position;

    public PawnViewState(PlayerViewState owner, int id) {
        this.owner = owner;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }

    public PlayerViewState getOwner() {
        return owner;
    }
}
