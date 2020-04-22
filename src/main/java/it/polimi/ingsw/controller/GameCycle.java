package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.model.player.User;
import it.polimi.ingsw.view.events.OnChoosePawnListener;
import it.polimi.ingsw.view.events.OnExecuteActionListener;

public class GameCycle implements OnExecuteActionListener, OnChoosePawnListener {

    @Override
    public boolean onExecuteAction(ActionIdentifier actionIdentifier, Coordinate coordinate) {
        return false;
    }


    @Override
    public void onChoosePawn(User user, int id) {

    }
}
