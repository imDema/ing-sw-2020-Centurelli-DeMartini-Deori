package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.User;

import java.util.List;

public interface OnActionsReadyListener {
    //Controller is ready to handle actions from user
    void onActionsReady(User user, List<ActionIdentifier> actions);
}
