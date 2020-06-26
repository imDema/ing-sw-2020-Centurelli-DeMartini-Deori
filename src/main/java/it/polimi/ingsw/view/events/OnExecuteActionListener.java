package it.polimi.ingsw.view.events;

import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Coordinate;

/**
 * Listener for an event that is launched when a user wants to execute an action during the game
 */
public interface OnExecuteActionListener {
    /**
     * Request to execute an action
     * @param user user that wants to execute the action
     * @param pawnId pawn to execute the action with
     * @param actionIdentifier identifier for the action
     * @param coordinate target coordinate for the action
     * @return true if the request was successful, false otherwise
     */
    boolean onExecuteAction(User user, int pawnId, ActionIdentifier actionIdentifier, Coordinate coordinate);
}
