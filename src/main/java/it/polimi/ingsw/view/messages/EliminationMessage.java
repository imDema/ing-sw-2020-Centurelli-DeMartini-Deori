package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.messages.User;

public class EliminationMessage implements Message{
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
}
