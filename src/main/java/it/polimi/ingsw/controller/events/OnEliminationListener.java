package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.messages.User;

public interface OnEliminationListener {
    void onElimination(User user);
}
