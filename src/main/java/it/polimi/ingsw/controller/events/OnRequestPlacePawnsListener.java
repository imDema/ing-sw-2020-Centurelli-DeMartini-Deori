package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.messages.User;

/**
 * Listener for an event that asks a user to choose a starting position
 */
public interface OnRequestPlacePawnsListener {
    void onRequestPlacePawns(User user);
}
