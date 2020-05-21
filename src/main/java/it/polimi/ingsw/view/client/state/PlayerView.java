package it.polimi.ingsw.view.client.state;

import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;

import java.util.ArrayList;
import java.util.List;

public class PlayerView {
    private final User user;
    private final List<PawnView> pawns = new ArrayList<>();
    private GodIdentifier god;


    public PlayerView(User user) {
        this.user = user;
    }

    public List<PawnView> getPawns() {
        return pawns;
    }

    public PawnView getPawn(int id){
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

    public void addPawn(PawnView pawn) {
        pawns.add(pawn);
    }
}
