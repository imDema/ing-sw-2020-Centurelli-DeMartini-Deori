package it.polimi.ingsw.view.events;

import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.model.player.User;

public interface OnPlacePawnListener {
    boolean onPlacePawn(User user, Coordinate coordinate);
}
