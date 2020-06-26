package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.messages.User;

/**
 * Listener for an event that notifies users of a user's login
 */
public interface OnUserJoinedListener {
    /**
     * A user has logged in and joined the lobby
     * @param user New user
     */
    void onUserJoined(User user);
}
