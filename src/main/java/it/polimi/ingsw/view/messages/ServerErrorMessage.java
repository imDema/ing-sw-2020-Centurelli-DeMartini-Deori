package it.polimi.ingsw.view.messages;

import static it.polimi.ingsw.view.messages.MessageId.SERVER_ERROR;

public class ServerErrorMessage implements Message {
    private final String type;
    private final String description;

    public ServerErrorMessage(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public MessageId getSerializationId() {
        return SERVER_ERROR;
    }

    @Override
    public boolean visit(MessageDispatcher dispatcher) {
        return dispatcher.onServerError(type, description);
    }
}
