package it.polimi.ingsw.controller.events;

/**
 * Listener for an event that informs clients of the size of the lobby and game
 */
public interface OnSizeSelectedListener {
    /**
     * The number of players for the game has been chosen
     * @param size number of players
     */
    void onSizeSelected(int size);
}
