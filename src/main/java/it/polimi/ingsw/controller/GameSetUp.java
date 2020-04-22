package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.view.events.OnAddUserListener;
import it.polimi.ingsw.view.events.OnChooseGodListener;
import it.polimi.ingsw.view.events.OnPlacePawnListener;
import it.polimi.ingsw.model.player.God;
import it.polimi.ingsw.model.player.User;

import java.util.Optional;

public class GameSetUp implements OnAddUserListener, OnPlacePawnListener, OnChooseGodListener {

    @Override
    public Optional<User> onAddUser(String username) {
        return Optional.empty();
    }

    @Override
    public boolean onChooseGod(User user, God god) {
        return false;
    }

    @Override
    public boolean onPlacePawn(User user, Coordinate coordinate) {
        return false;
    }
}
