package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.messages.User;

public class WinMessage implements Message{
    private final User user;

    public WinMessage(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public MessageId getSerializationId() {
        return MessageId.WIN;
    }
}
