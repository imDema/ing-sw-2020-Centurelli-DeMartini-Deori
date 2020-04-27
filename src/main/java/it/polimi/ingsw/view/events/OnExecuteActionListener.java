package it.polimi.ingsw.view.events;

import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.model.board.Coordinate;

public interface OnExecuteActionListener {
    // Returns true if successful
    boolean onExecuteAction(ActionIdentifier actionIdentifier, Coordinate coordinate);
}
