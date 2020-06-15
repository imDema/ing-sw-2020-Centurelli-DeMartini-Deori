package it.polimi.ingsw.controller.events;

/**
 * Listener for an unrecoverable error that will result in the termination of the game
 */
public interface OnServerErrorListener {
    void onServerError(String type, String description);
}
