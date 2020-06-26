package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.messages.GodIdentifier;

import java.util.List;

/**
 * Listener for an event that communicates the list of gods that are currently available
 */
public interface OnGodsAvailableListener {
    /**
     * The list of available gods has been updated
     * @param gods currently available gods
     */
    void onGodsAvailable(List<GodIdentifier> gods);
}
