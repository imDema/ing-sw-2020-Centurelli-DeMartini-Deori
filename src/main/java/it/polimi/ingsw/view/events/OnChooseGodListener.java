package it.polimi.ingsw.view.events;

import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;

/**
 * Listener for an event that is launched when a client wants to choose a god card for himself
 */
public interface OnChooseGodListener {
    boolean onChooseGod(User user, GodIdentifier god);
}
