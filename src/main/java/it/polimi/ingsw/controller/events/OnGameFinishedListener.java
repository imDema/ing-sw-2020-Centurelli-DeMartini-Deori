package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.GameController;

/**
 * Listener for an event that is triggered when a match has finished
 */
public interface OnGameFinishedListener {
    void onGameFinished(GameController controller);
}
