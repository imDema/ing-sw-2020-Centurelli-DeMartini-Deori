package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.messages.User;

public interface OnTurnChangeListener {
    void onTurnChange(User currentUser, int turn);
}
