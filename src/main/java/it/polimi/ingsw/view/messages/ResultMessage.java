package it.polimi.ingsw.view.messages;

public class ResultMessage implements Message {
    private final boolean value;

    public ResultMessage(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public MessageId getSerializationId() {
        return MessageId.RESULT;
    }
}
