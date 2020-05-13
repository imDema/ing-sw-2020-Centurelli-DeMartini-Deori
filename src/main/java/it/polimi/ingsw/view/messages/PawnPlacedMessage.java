package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.events.ServerEventsListener;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Coordinate;

public class PawnPlacedMessage implements ServerMessage {
    private final User owner;
    private final int pawnId;
    private final Coordinate coordinate;

    public User getOwner() {
        return owner;
    }

    public int getPawnId() {
        return pawnId;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public PawnPlacedMessage(User owner, int pawnId, Coordinate coordinate) {
        this.owner = owner;
        this.pawnId = pawnId;
        this.coordinate = coordinate;
    }

    @Override
    public MessageId getSerializationId() {
        return MessageId.PAWN_PLACED;
    }

    @Override
    public void visit(ServerEventsListener listener) {
        listener.onPawnPlaced(owner, pawnId, coordinate);
    }
}
