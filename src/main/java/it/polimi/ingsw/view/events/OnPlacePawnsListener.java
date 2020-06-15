package it.polimi.ingsw.view.events;

import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Coordinate;

/**
 * Listener for an event that is launched when a user wants to select the starting positions for his pawns
 */
public interface OnPlacePawnsListener {
    boolean onPlacePawns(User user, Coordinate c1, Coordinate c2);
}
