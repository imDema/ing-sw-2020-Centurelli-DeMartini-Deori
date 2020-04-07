package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.board.Coordinate;

public class Pawn {
    private Player owner;
    private int id;

    public Player getOwner() {
        return owner;
    }

    public int getId() {
        return id;
    }

    private Coordinate position;

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pawn) {
            Pawn p = (Pawn) obj;
            return id == p.id && owner.equals(p.owner);
        }
        return  false;
    }

    public Pawn(Player owner, int id) {
        this.owner = owner;
        this.id = id;
    }
}
