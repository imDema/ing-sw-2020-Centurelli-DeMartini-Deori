package it.polimi.ingsw.view.events;

import it.polimi.ingsw.controller.messages.User;

public interface OnChoosePawnListener {
    void onChoosePawn(User user, int id);
}