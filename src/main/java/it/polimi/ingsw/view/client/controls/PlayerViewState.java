package it.polimi.ingsw.view.client.controls;

import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Client side player state representation
 */
public class PlayerViewState {
    private final User user;
    private final List<PawnViewState> pawns = new ArrayList<>();
    private GodIdentifier god;

    public PlayerViewState(User user) {
        this.user = user;
    }

    public List<PawnViewState> getPawns() {
        return pawns;
    }

    public PawnViewState getPawn(int id){
        return pawns.get(id);
    }

    public User getUser() {
        return user;
    }

    public GodIdentifier getGod() {
        return god;
    }

    public void setGod(GodIdentifier god) {
        this.god = god;
    }

    public void addPawn(PawnViewState pawn) {
        pawns.add(pawn);
    }
}
