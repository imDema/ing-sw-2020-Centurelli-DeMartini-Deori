package it.polimi.ingsw.view.events;

import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Coordinate;

public interface OnCheckActionListener {
    // Returns true if action is allowed
    boolean onCheckAction(User user, int pawnId, ActionIdentifier actionIdentifier, Coordinate coordinate);
}
