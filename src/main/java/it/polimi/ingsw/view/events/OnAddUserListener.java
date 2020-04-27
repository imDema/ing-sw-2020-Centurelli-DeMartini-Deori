package it.polimi.ingsw.view.events;

import it.polimi.ingsw.controller.messages.User;

import java.util.Optional;

public interface OnAddUserListener {
    Optional<User> onAddUser(String username);
}
