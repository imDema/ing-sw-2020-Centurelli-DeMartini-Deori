package it.polimi.ingsw.view.events;

import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Coordinate;

/**
 * Listener for an event that is launched when a user wants to select the starting positions for his pawns
 */
public interface OnPlacePawnsListener {
    /**
     * Request to place the pawns for a user
     * @param user User making the request
     * @param c1 Coordinate for the first pawn
     * @param c2 Coordinate for the second pawn
     * @return true if successful, false otherwise
     */
    boolean onPlacePawns(User user, Coordinate c1, Coordinate c2);
}
