package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.messages.User;

/**
 * Listener for an event that notifies of a user's elimination from the game
 */
public interface OnEliminationListener {
    void onElimination(User user);
}
