package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.board.Coordinate;

/**
 * Model representation of a worker pawn.
 */
public class Pawn {
    private final Player owner;
    private final int id;

    /**
     * Get the player that owns this pawn
     * @return owner of the pawn
     */
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
