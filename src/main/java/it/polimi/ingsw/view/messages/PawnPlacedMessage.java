package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Coordinate;

import static it.polimi.ingsw.view.messages.MessageId.PAWN_PLACED;

public class PawnPlacedMessage implements Message {
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
        return PAWN_PLACED;
    }

    @Override
    public boolean visit(MessageDispatcher dispatcher) {
        return dispatcher.onPawnPlaced(owner, pawnId, coordinate);
    }
}
