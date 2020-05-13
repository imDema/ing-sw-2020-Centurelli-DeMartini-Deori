package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.events.ServerEventsListener;

public class ServerErrorMessage implements ServerMessage {
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
        return MessageId.SERVER_ERROR;
    }

    @Override
    public void visit(ServerEventsListener listener) {
        listener.onServerError(type, description);
    }
}
