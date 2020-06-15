package it.polimi.ingsw.controller.events;

/**
 * Listener for an event that informs clients of the size of the lobby and game
 */
public interface OnSizeSelectedListener {
    void onSizeSelected(int size);
}
