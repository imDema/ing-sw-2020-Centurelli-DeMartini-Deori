package it.polimi.ingsw.controller.events;

/**
 * Listener for an unrecoverable error that will result in the termination of the game
 */
public interface OnServerErrorListener {
    /**
     * An unrecoverable error has occurred, the game is being terminated
     * @param type Error type
     * @param description Error details
     */
    void onServerError(String type, String description);
}
