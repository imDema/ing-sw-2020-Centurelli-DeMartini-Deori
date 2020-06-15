package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.User;

import java.util.List;

/**
 * Listener for an event that notifies which user should execute an action and which actions it's allowed to execute
 */
public interface OnActionsReadyListener {
    //Controller is ready to handle actions from user
    void onActionsReady(User user, List<ActionIdentifier> actions);
}
