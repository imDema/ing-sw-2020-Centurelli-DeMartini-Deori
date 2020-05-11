package it.polimi.ingsw.view.messages;

public class ServerErrorMessage implements  Message {
    private String type;
    private String description;

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
}
