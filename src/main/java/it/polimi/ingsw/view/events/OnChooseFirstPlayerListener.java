package it.polimi.ingsw.view.events;

import it.polimi.ingsw.controller.messages.User;

/**
 * Listener for an event that is launched when a user wants to choose who should be the one to start placing pawns
 */
public interface OnChooseFirstPlayerListener {
    boolean onChooseFirstPlayer(User self, User firstPlayer);
}
