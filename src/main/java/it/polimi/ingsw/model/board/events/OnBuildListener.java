package it.polimi.ingsw.model.board.events;

import it.polimi.ingsw.model.board.Building;
import it.polimi.ingsw.model.board.Coordinate;

/**
 * Listener for an event that is launched when a building on the board has changed
 */
public interface OnBuildListener {
    /**
     * @param building updated state of the building
     * @param coordinate coordinate of the build
     */
    void onBuild(Building building, Coordinate coordinate);
}
