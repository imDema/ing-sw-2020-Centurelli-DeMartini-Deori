package it.polimi.ingsw.view.events;

import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.model.board.Coordinate;

public interface OnExecuteActionListener {
    boolean onExecuteAction(ActionIdentifier actionIdentifier, Coordinate coordinate);
}
