package it.polimi.ingsw.controller.events;

import it.polimi.ingsw.controller.messages.GodIdentifier;

import java.util.List;

public interface OnGodsAvailableListener {
    void onGodsAvailable(List<GodIdentifier> gods);
}
