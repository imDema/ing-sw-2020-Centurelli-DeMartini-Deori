package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.view.events.ClientEventsListener;

public class SelectPlayerNumberMessage implements ClientMessage {
    private final int size;

    public SelectPlayerNumberMessage(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    @Override
    public MessageId getSerializationId() {
        return MessageId.SELECT_PLAYER_NUMBER;
    }

    @Override
    public boolean visit(ClientEventsListener listener) {
        return listener.onSelectPlayerNumber(size);
    }
}
