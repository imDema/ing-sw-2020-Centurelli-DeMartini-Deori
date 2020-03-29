package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.action.ActionKind;
import it.polimi.ingsw.model.board.Coordinate;

import java.util.List;

public class Pawn {
    private Player owner;
    private int id;

    public boolean tryAction(ActionKind a, Coordinate c) {
        throw new UnsupportedOperationException();
    }

    public boolean execute(ActionKind a, Coordinate c) {
        throw new UnsupportedOperationException();
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
