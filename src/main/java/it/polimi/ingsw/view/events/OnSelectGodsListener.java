package it.polimi.ingsw.view.events;

import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;

import java.util.List;

/**
 * Listener for an event that is launched when a user wants to select the list of gods that will be available for this game
 */
public interface OnSelectGodsListener {
    /**
     * Request to select the gods that will be available for the game
     * @param user user making the request
     * @param selectedGods list of gods that should br available
     * @return true if successful, false otherwise
     */
    boolean onSelectGods(User user, List<GodIdentifier> selectedGods);
}
