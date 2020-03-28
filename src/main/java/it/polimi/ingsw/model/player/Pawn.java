package it.polimi.ingsw.model.player;

public class Pawn {
    private Player owner;
    private int id;

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
