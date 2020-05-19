package it.polimi.ingsw.view.messages;

import static it.polimi.ingsw.view.messages.MessageId.SELECT_PLAYER_NUMBER;

public class SelectPlayerNumberMessage implements Message {
    private final int size;

    public SelectPlayerNumberMessage(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    @Override
    public MessageId getSerializationId() {
        return SELECT_PLAYER_NUMBER;
    }

    @Override
    public boolean visit(MessageDispatcher dispatcher) {
        return dispatcher.onSelectPlayerNumber(size);
    }
}
