package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.events.ServerEventsListener;
import it.polimi.ingsw.controller.messages.User;

public class EliminationMessage implements ServerMessage {
    private final User user;

    public User getUser() {
        return user;
    }

    public EliminationMessage(User user) {
        this.user = user;
    }

    @Override
    public MessageId getSerializationId() {
        return MessageId.ELIMINATION;
    }

    @Override
    public void visit(ServerEventsListener listener) {
        listener.onElimination(user);
    }
}
