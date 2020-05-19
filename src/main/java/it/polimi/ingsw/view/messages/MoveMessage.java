package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.model.board.Coordinate;

import static it.polimi.ingsw.view.messages.MessageId.MOVE;

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
        return MOVE;
    }

    @Override
    public boolean visit(MessageDispatcher dispatcher) {
        return dispatcher.onMove(from, to);
    }
}
