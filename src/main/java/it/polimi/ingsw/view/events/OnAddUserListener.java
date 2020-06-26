package it.polimi.ingsw.view.events;

import it.polimi.ingsw.controller.messages.User;

/**
 * Listener for an event that is launched when a client wants to login and add its user to the lobby
 */
public interface OnAddUserListener {
    /**
     * Request to add a new user to the lobby
     * @param user User to add
     * @return true if successful, false otherwise
     */
    boolean onAddUser(User user);
}
