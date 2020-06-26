package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.messages.User;
/**
 * Listener for an event that is launched when a user has won the game
 */
public interface OnWinListener {
    /**
     * A user has won the game
     * @param user Winner
     */
    void onWin(User user);
}
