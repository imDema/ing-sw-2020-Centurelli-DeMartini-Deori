package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;

/**
 * Listener for an event that notifies that a user has chosen a god
 */
public interface OnGodChosenListener {
    void onGodChosen(User user, GodIdentifier godIdentifier);
}
