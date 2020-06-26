package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.messages.User;

/**
 * Listener for an event that signals a change of turn
 */
public interface OnTurnChangeListener {
    /**
     * The turn has changed
     * @param currentUser Current user
     * @param turn Turn number, starting at 0
     */
    void onTurnChange(User currentUser, int turn);
}
