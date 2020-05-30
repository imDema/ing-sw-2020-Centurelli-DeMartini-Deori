package it.polimi.ingsw.view.events;

import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;

import java.util.List;

public interface OnSelectGodsListener {
    boolean onSelectGods(User user, List<GodIdentifier> selectedGods);
}
