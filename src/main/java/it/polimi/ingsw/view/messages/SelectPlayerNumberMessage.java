package it.polimi.ingsw.view.messages;

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
        return MessageId.SELECT_PLAYER_NUMBER;
    }
}
