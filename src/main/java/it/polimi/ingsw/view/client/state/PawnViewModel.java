package it.polimi.ingsw.view.client.state;

import it.polimi.ingsw.model.board.Coordinate;

public class PawnViewModel {
    private final PlayerViewModel owner;
    private final int id;
    private Coordinate position;

    public PlayerViewModel getOwner() {
        return owner;
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

    public PawnViewModel(PlayerViewModel owner, int id) {
        this.owner = owner;
        this.id = id;
    }
}
