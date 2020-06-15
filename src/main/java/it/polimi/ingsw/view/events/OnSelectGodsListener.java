package it.polimi.ingsw.view.events;

import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;

import java.util.List;

/**
 * Listener for an event that is launched when a user wants to select the list of gods that will be available for this game
 */
public interface OnSelectGodsListener {
    boolean onSelectGods(User user, List<GodIdentifier> selectedGods);
}
