package it.polimi.ingsw.view.events;

import it.polimi.ingsw.controller.messages.User;

public interface OnChooseFirstPlayerListener {
    boolean onChooseFirstPlayer(User self, User firstPlayer);
}
