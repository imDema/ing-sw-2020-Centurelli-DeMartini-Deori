package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.messages.GodIdentifier;

import java.util.List;

import static it.polimi.ingsw.view.messages.MessageId.GODS_AVAILABLE;

public class GodsAvailableMessage implements Message {
    private final List<GodIdentifier> gods;

    public GodsAvailableMessage(List<GodIdentifier> gods) {
        this.gods = gods;
    }

    public List<GodIdentifier> getGods() {
        return gods;
    }

    @Override
    public MessageId getSerializationId() {
        return GODS_AVAILABLE;
    }

    @Override
    public boolean visit(MessageDispatcher dispatcher) {
        return dispatcher.onGodsAvailable(gods);
    }
}
