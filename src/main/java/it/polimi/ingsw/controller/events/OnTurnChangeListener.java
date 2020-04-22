package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.model.player.User;

public interface OnTurnChangeListener {
    void onTurnChange(User currentUser, int turn);
}
