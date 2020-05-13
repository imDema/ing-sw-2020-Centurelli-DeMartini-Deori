package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.events.ServerEventsListener;
import it.polimi.ingsw.controller.messages.User;

public class WinMessage implements ServerMessage {
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

    @Override
    public void visit(ServerEventsListener listener) {
        listener.onWin(user);
    }
}
