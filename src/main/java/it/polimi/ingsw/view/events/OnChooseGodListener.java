package it.polimi.ingsw.view.events;

import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;

public interface OnChooseGodListener {
    boolean onChooseGod(User user, GodIdentifier god);
}
