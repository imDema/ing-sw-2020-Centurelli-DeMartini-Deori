package it.polimi.ingsw.model.board.events;

import it.polimi.ingsw.model.board.Coordinate;

public interface OnMoveListener {
    void onMove(Coordinate from, Coordinate to);
}
