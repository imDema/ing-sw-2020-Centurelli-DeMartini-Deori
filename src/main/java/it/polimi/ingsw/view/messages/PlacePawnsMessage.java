package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Coordinate;

public class PlacePawnsMessage implements Message{
    private final User user;
    private final Coordinate c1;
    private final Coordinate c2;

    public PlacePawnsMessage(User user, Coordinate c1, Coordinate c2) {
        this.user = user;
        this.c1 = c1;
        this.c2 = c2;
    }

    public User getUser() {
        return user;
    }

    public Coordinate getC1() {
        return c1;
    }

    public Coordinate getC2() {
        return c2;
    }

    @Override
    public MessageId getSerializationId() {
        return MessageId.PLACE_PAWNS;
    }
}
