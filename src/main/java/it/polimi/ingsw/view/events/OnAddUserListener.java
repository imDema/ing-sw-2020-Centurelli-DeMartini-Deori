package it.polimi.ingsw.view.events;

import it.polimi.ingsw.model.player.User;

import java.util.Optional;

public interface OnAddUserListener {
    Optional<User> onAddUser(String username);
}
