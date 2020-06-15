package it.polimi.ingsw.view.events;

import it.polimi.ingsw.controller.messages.User;

/**
 * Listener for an event that is launched when a client wants to login and add its user to the lobby
 */
public interface OnAddUserListener {
    boolean onAddUser(User user);
}
