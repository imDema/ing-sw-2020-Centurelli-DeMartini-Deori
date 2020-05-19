package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.messages.User;

import static it.polimi.ingsw.view.messages.MessageId.USER_JOINED;

public class UserJoinedMessage implements Message {
    private final User user;

    public UserJoinedMessage(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public MessageId getSerializationId() {
        return USER_JOINED;
    }

    @Override
    public boolean visit(MessageDispatcher dispatcher) {
        return dispatcher.onUserJoined(user);
    }
}
