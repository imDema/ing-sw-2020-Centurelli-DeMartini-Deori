package it.polimi.ingsw.view.events;

import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;

/**
 * Listener for an event that is launched when a client wants to choose a god card for himself
 */
public interface OnChooseGodListener {
    /**
     * Request to choose a god
     * @param user User making the request
     * @param god Chosen god
     * @return true if successful, false otherwise
     */
    boolean onChooseGod(User user, GodIdentifier god);
}
