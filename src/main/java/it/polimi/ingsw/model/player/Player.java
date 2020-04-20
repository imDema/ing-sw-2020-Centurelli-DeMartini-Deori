package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.action.Action;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String username;
    private Pawn[] pawns = new Pawn[2];
    private God god;

    public List<Action> nextStep(Action action) {
        if (action.equals(Action.start))
            god.getTurnSequence().start();
        else
            god.getTurnSequence().nextStep(action);

        return god.getTurnSequence().getStep();
    }

    public String getUsername() {
        return username;
    }

    public Pawn getPawn(int id) {
        return pawns[id];
    }

    public void setGod(God god) {
        this.god = god;
    }

    public God getGod() {
        return god;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Player && username.equals(((Player) obj).username);
    }

    public Player(String username) {
        this.username = username;
        this.pawns[0] = new Pawn(this, 0);
        this.pawns[1] = new Pawn(this, 1);
    }
}
