package it.polimi.ingsw.model.board.events;

import it.polimi.ingsw.model.board.Building;
import it.polimi.ingsw.model.board.Coordinate;

public interface OnBuildListener {
    void onBuild(Building building, Coordinate coordinate);
}
