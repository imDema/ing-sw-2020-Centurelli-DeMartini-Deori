package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.GameController;

/**
 * Listener for an event that is triggered when a match has finished
 */
public interface OnGameFinishedListener {
    /**
     * A game has finished
     * @param controller controller that was handling the game
     */
    void onGameFinished(GameController controller);
}
