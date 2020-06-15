package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Coordinate;

/**
 * Listener for an event that notifies the placement of a user's pawn with their corresponding starting position
 */
public interface OnPawnPlacedListener {
    void onPawnPlaced(User owner, int pawnId, Coordinate coordinate);
}
