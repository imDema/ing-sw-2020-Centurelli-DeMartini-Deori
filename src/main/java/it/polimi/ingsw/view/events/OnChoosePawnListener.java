package it.polimi.ingsw.view.events;

import it.polimi.ingsw.model.player.User;

public interface OnChoosePawnListener {
    void onChoosePawn(User user, int id);
}
