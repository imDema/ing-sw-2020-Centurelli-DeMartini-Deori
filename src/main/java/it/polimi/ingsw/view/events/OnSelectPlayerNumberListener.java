package it.polimi.ingsw.view.events;

/**
 * Listener for an event that is launched when a user wants to select the size of the lobby and game
 */
public interface OnSelectPlayerNumberListener {
    boolean onSelectPlayerNumber(int size);
}
