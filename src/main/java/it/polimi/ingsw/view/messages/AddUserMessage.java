package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.view.events.ClientEventsListener;

public class AddUserMessage implements ClientMessage {
    private final User user;

    public User getUser() {
        return user;
    }

    public AddUserMessage(User user){
        this.user = user;
    }
    public AddUserMessage(String name) {
        this(new User(name));
    }

    @Override
    public MessageId getSerializationId() {
        return MessageId.ADD_USER;
    }

    @Override
    public boolean visit(ClientEventsListener listener) {
        return listener.onAddUser(user);
    }
}
