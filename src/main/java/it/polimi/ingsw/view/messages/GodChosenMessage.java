package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;

import static it.polimi.ingsw.view.messages.MessageId.GOD_CHOSEN;

public class GodChosenMessage implements Message {
    private final User user;
    private final GodIdentifier godIdentifier;

    public GodChosenMessage(User user, GodIdentifier godIdentifier) {
        this.user = user;
        this.godIdentifier = godIdentifier;
    }

    public User getUser() {
        return user;
    }

    public GodIdentifier getGodIdentifier() {
        return godIdentifier;
    }

    @Override
    public MessageId getSerializationId() {
        return GOD_CHOSEN;
    }

    @Override
    public boolean visit(MessageDispatcher dispatcher) {
        return dispatcher.onGodChosen(user, godIdentifier);
    }
}
