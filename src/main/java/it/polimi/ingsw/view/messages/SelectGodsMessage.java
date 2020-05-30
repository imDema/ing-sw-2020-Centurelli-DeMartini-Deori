package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;

import java.util.List;

public class SelectGodsMessage implements Message {
    private final User user;
    private final List<GodIdentifier> gods;

    public User getUser() {
        return user;
    }

    public List<GodIdentifier> getGods() {
        return gods;
    }

    public SelectGodsMessage(User user, List<GodIdentifier> gods) {
        this.user = user;
        this.gods = gods;
    }

    @Override
    public MessageId getSerializationId() {
        return MessageId.SELECT_GODS;
    }

    @Override
    public boolean visit(MessageDispatcher dispatcher) {
        return dispatcher.onSelectGods(user, gods);
    }
}
