package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.User;

import java.util.List;

/**
 * Listener for an event that notifies which user should execute an action and which actions it's allowed to execute
 */
public interface OnActionsReadyListener {
    /**
     * Actions are ready for a user and he should now execute one.
     * @param user User that should execute the action
     * @param actions List of actions available to the user
     */
    void onActionsReady(User user, List<ActionIdentifier> actions);
}
