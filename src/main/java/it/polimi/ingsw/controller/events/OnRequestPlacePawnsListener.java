package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.messages.User;

/**
 * Listener for an event that asks a user to choose a starting position
 */
public interface OnRequestPlacePawnsListener {
    /**
     * A user must now place its pawns
     * @param user User that must place
     */
    void onRequestPlacePawns(User user);
}
