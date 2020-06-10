package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.board.Coordinate;

/**
 * The Pawn class represents the abstraction of a pawn in the game,
 * a pawn have an owner (the player that owns the pawn) and an ID,
 * the pair (owner,ID) is unique for every pawn in the game.
 * Every pawn also have a coordinate that tells where the pawn is
 * located on the game board
 */
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
