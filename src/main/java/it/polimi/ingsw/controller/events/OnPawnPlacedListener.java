package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Coordinate;

/**
 * Listener for an event that notifies the placement of a user's pawn with their corresponding starting position
 */
public interface OnPawnPlacedListener {
    /**
     * A pawn has been placed
     * @param owner Owner of the pawn
     * @param pawnId Id of the pawn
     * @param coordinate Pawn position
     */
    void onPawnPlaced(User owner, int pawnId, Coordinate coordinate);
}
