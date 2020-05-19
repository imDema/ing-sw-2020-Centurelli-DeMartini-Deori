package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.messages.User;

import static it.polimi.ingsw.view.messages.MessageId.WIN;

public class WinMessage implements Message {
    private final User user;

    public WinMessage(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public MessageId getSerializationId() {
        return WIN;
    }

    @Override
    public boolean visit(MessageDispatcher dispatcher) {
        return dispatcher.onWin(user);
    }
}
