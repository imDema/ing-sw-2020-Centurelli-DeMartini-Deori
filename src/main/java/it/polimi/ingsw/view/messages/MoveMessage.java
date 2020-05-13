package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.events.ServerEventsListener;
import it.polimi.ingsw.model.board.Coordinate;

public class MoveMessage implements ServerMessage {
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

    @Override
    public void visit(ServerEventsListener listener) {
        listener.onMove(from, to);
    }
}
