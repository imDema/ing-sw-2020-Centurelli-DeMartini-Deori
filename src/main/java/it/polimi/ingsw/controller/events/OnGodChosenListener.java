package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;

public interface OnGodChosenListener {
    void onGodChosen(User user, GodIdentifier godIdentifier);
}
