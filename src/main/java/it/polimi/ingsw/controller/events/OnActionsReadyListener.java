package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.model.player.User;

public interface OnActionsReadyListener {
    //Controller is ready to handle actions from user
    void onActionsReady(User user, ActionIdentifier[] actions);
}
