package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;

/**
 * Listener for an event that notifies that a user has chosen a god
 */
public interface OnGodChosenListener {
    /**
     * A user has chosen a god
     * @param user User that chose its god
     * @param godIdentifier God chosen
     */
    void onGodChosen(User user, GodIdentifier godIdentifier);
}
