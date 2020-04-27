package it.polimi.ingsw.view.events;

import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.controller.messages.User;

public interface OnPlacePawnsListener {
    boolean onPlacePawns(User user, Coordinate c1, Coordinate c2);
}
