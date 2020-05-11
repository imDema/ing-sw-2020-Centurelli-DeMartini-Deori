package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.messages.User;

public interface OnUserJoinedListener {
    void onUserJoined(User user);
}
