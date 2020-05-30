package it.polimi.ingsw.view.messages;

public class SizeSelectedMessage implements Message {
    private final int size;

    public int getSize() {
        return size;
    }

    public SizeSelectedMessage(int size) {
        this.size = size;
    }

    @Override
    public MessageId getSerializationId() {
        return MessageId.SIZE_SELECTED;
    }

    @Override
    public boolean visit(MessageDispatcher dispatcher) {
        return dispatcher.onSizeSelected(size);
    }
}
