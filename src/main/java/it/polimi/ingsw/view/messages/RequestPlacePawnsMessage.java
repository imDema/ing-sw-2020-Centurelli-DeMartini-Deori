package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.messages.User;

import static it.polimi.ingsw.view.messages.MessageId.REQUEST_PLACE_PAWNS;

public class RequestPlacePawnsMessage implements Message {
    private final User user;

    public User getUser() {
        return user;
    }

    public RequestPlacePawnsMessage(User user) {
        this.user = user;
    }

    @Override
    public MessageId getSerializationId() {
        return REQUEST_PLACE_PAWNS;
    }

    @Override
    public boolean visit(MessageDispatcher dispatcher) {
        return dispatcher.onRequestPlacePawns(user);
    }
}
