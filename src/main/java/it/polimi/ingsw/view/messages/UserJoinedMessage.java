package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.messages.User;

public class UserJoinedMessage implements Message{
    private User user;

    public UserJoinedMessage(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public MessageId getSerializationId() {
        return MessageId.USER_JOINED;
    }
}
