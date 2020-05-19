package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.messages.User;

import static it.polimi.ingsw.view.messages.MessageId.ELIMINATION;

public class EliminationMessage implements Message {
    private final User user;

    public User getUser() {
        return user;
    }

    public EliminationMessage(User user) {
        this.user = user;
    }

    @Override
    public MessageId getSerializationId() {
        return ELIMINATION;
    }

    @Override
    public boolean visit(MessageDispatcher dispatcher) {
        return dispatcher.onElimination(user);
    }
}
