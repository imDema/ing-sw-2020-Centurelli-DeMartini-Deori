package it.polimi.ingsw.view.client.state;

import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;

import java.util.ArrayList;
import java.util.List;

public class PlayerViewModel {
    private final User user;
    private final List<PawnViewModel> pawns = new ArrayList<>();
    private GodIdentifier god;


    public PlayerViewModel(User user) {
        this.user = user;
    }

    public List<PawnViewModel> getPawns() {
        return pawns;
    }

    public PawnViewModel getPawn(int id){
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

    public void addPawn(PawnViewModel pawn) {
        pawns.add(pawn);
    }
}
