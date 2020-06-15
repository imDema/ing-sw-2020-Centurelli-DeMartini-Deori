package it.polimi.ingsw.model.board.events;

import it.polimi.ingsw.model.board.Coordinate;

/**
 * Listener for an event that is launched when a pawn has moved on the board
 */
public interface OnMoveListener {
    /**
     * A pawn has moved. If there are pawns on both coordinates they must be swapped.
     * @param from coordinate of the pawn that has moved
     * @param to destination of the movement
     */
    void onMove(Coordinate from, Coordinate to);
}
