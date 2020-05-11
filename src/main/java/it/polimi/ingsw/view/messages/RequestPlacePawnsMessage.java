package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.messages.User;

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
        return MessageId.REQUEST_PLACE_PAWNS;
    }
}
