package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.model.player.User;

public interface OnEliminationListener {
    void onElimination(User user);
}
