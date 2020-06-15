package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.messages.User;

/**
 * Listener for an event that signals a change of turn
 */
public interface OnTurnChangeListener {
    void onTurnChange(User currentUser, int turn);
}
