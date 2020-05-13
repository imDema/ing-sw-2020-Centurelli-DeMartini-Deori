package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.events.ServerEventsListener;
import it.polimi.ingsw.controller.messages.User;

public class RequestPlacePawnsMessage implements ServerMessage {
    private final User user;

    public User getUser() {
        return user;
    }

    public RequestPlacePawnsMessage(User user) {
        this.user = user;
    }

    @Override
    public MessageId getSerializationId() {
        return MessageId.REQUEST_PLACE_PAWNS;
    }

    @Override
    public void visit(ServerEventsListener listener) {
        listener.onRequestPlacePawns(user);
    }
}
