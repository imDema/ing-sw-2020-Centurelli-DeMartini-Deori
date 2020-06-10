package it.polimi.ingsw.model.player;

import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.action.Action;

/**
 * The Player class represents a player of the game, each player has a
 * username, a god card and two pawns
 */
public class Player {
    private final String username;
    private final Pawn[] pawns = new Pawn[2];
    private final God god;

    public Action[] nextStep(Action action) {
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

    public God getGod() {
        return god;
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Player && username.equals(((Player) obj).username);
    }

    public Player(String username, God god) {
        this.username = username;
        this.pawns[0] = new Pawn(this, 0);
        this.pawns[1] = new Pawn(this, 1);
        this.god = god;
    }
    public Player(User user, God god){
        this(user.getUsername(), god);
    }
}
