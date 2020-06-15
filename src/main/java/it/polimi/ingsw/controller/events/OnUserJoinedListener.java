package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.messages.User;

/**
 * Listener for an event that notifies users of a user's login
 */
public interface OnUserJoinedListener {
    void onUserJoined(User user);
}
