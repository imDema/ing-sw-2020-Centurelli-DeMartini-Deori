package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.events.ServerEventsListener;
import it.polimi.ingsw.controller.messages.GodIdentifier;

import java.util.List;

public class GodsAvailableMessage implements ServerMessage {
    private final List<GodIdentifier> gods;

    public GodsAvailableMessage(List<GodIdentifier> gods) {
        this.gods = gods;
    }

    public List<GodIdentifier> getGods() {
        return gods;
    }

    @Override
    public MessageId getSerializationId() {
        return MessageId.GODS_AVAILABLE;
    }

    @Override
    public void visit(ServerEventsListener listener) {
        listener.onGodsAvailable(gods);
    }
}
