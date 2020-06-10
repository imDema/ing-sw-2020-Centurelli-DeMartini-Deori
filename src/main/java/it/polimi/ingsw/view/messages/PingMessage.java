package it.polimi.ingsw.view.messages;

public class PingMessage implements Message {

    @Override
    public MessageId getSerializationId() {
        return MessageId.PING;
    }

    @Override
    public boolean visit(MessageDispatcher dispatcher) {
        return dispatcher.onPing();
    }
}
