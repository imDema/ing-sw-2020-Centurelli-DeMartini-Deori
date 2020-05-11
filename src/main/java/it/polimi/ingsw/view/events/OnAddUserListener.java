package it.polimi.ingsw.view.events;

import it.polimi.ingsw.controller.messages.User;

public interface OnAddUserListener {
    boolean onAddUser(User user);
}
