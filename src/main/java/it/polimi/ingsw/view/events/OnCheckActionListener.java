package it.polimi.ingsw.view.events;

import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Coordinate;

/**
 * Listener for an event that is launched when a user wants to know if an action is valid
 */
public interface OnCheckActionListener {
    /**
     * Request to check if an action is allowed
     * @param user user that wants to execute the action
     * @param pawnId pawn to execute the action with
     * @param actionIdentifier identifier for the action that should be checked
     * @param coordinate target coordinate for the action
     * @return true if the action is valid, false otherwise
     */
    boolean onCheckAction(User user, int pawnId, ActionIdentifier actionIdentifier, Coordinate coordinate);
}
