package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.action.ActionKind;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<OnWinListener> onWinListeners = new ArrayList<>();
    private List<OnLossListener> onLossListeners = new ArrayList<>();

    private String username;
    private Pawn[] pawns = new Pawn[2];
    private God god;

    // Reset TurnSequence at turn start
    public void startTurn() {
        god.getTurnSequence().start();
    }

    public List<ActionKind> getAllowedActions() {
        throw new UnsupportedOperationException();
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

    public void addOnWinListener(OnWinListener listener) {
        onWinListeners.add(listener);
    }
    public void addOnLossListener(OnLossListener listener){
        onLossListeners.add(listener);
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
