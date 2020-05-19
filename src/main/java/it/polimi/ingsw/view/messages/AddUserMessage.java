package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.messages.User;

import static it.polimi.ingsw.view.messages.MessageId.ADD_USER;

public class AddUserMessage implements Message {
    private final User user;

    public User getUser() {
        return user;
    }

    public AddUserMessage(User user) {
        this.user = user;
    }

    public AddUserMessage(String name) {
        this(new User(name));
    }

    @Override
    public MessageId getSerializationId() {
        return ADD_USER;
    }

    @Override
    public boolean visit(MessageDispatcher dispatcher) {
        return dispatcher.onAddUser(user);
    }
}
