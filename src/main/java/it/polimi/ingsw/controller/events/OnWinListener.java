package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.messages.User;

public interface OnWinListener {
    void onWin(User user);
}