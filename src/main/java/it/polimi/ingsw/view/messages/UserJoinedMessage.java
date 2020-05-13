package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.events.ServerEventsListener;
import it.polimi.ingsw.controller.messages.User;

public class UserJoinedMessage implements ServerMessage {
    private final User user;

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

    @Override
    public void visit(ServerEventsListener listener) {
        listener.onUserJoined(user);
    }
}
