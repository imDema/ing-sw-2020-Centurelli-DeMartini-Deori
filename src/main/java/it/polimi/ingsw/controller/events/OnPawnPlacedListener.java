package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Coordinate;

public interface OnPawnPlacedListener {
    void onPawnPlaced(User owner, int pawnId, Coordinate coordinate);
}
