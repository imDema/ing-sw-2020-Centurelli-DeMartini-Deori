package it.polimi.ingsw.view.events;

import it.polimi.ingsw.model.player.God;
import it.polimi.ingsw.model.player.User;

public interface OnChooseGodListener {
    boolean onChooseGod(User user, God god);
}
