package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.messages.User;

/**
 * Listener for an event that notifies of a user's elimination from the game
 */
public interface OnEliminationListener {
    /**
     * A user has been eliminated
     * @param user user that has been eliminated
     */
    void onElimination(User user);
}
