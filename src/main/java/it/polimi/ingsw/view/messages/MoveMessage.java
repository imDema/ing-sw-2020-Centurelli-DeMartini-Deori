package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.model.board.Coordinate;

public class MoveMessage implements Message {
    private final Coordinate from;
    private final Coordinate to;

    public MoveMessage(Coordinate from, Coordinate to) {
        this.from = from;
        this.to = to;
    }

    public Coordinate getSource() {
        return from;
    }

    public Coordinate getDestination() {
        return to;
    }

    @Override
    public MessageId getSerializationId() {
        return MessageId.MOVE;
    }
}
