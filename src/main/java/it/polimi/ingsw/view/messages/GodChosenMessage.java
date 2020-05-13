package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.events.ServerEventsListener;
import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;

public class GodChosenMessage implements ServerMessage {
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
        return MessageId.GOD_CHOSEN;
    }

    @Override
    public void visit(ServerEventsListener listener) {
        listener.onGodChosen(user, godIdentifier);
    }
}
