package it.polimi.ingsw.view.events;

import it.polimi.ingsw.controller.messages.User;

/**
 * Listener for an event that is launched when a user wants to choose who should be the one to start placing pawns
 */
public interface OnChooseFirstPlayerListener {
    /**
     * Request to choose the first player to place its pawns and move
     * @param self User making the request
     * @param firstPlayer User that should play first
     * @return true if successful, false otherwise
     */
    boolean onChooseFirstPlayer(User self, User firstPlayer);
}
