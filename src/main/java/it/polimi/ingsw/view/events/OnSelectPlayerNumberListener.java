package it.polimi.ingsw.view.events;

/**
 * Listener for an event that is launched when a user wants to select the size of the lobby and game
 */
public interface OnSelectPlayerNumberListener {
    /**
     * Request to choose the size of the game
     * @param size Number of players for the game
     * @return true if successful, false otherwise
     */
    boolean onSelectPlayerNumber(int size);
}
