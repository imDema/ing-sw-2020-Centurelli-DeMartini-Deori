package it.polimi.ingsw.view.events;

import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Coordinate;

/**
 * Listener for an event that is launched when a user wants to know if an action is valid
 */
public interface OnCheckActionListener {
    // Returns true if action is allowed
    boolean onCheckAction(User user, int pawnId, ActionIdentifier actionIdentifier, Coordinate coordinate);
}
