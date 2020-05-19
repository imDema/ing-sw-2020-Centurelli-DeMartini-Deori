package it.polimi.ingsw.view.messages;

public interface Message {
    MessageId getSerializationId();

    boolean visit(MessageDispatcher dispatcher);
}
