package it.polimi.ingsw.view.messages;

public class AddUserMessage implements Message {
    private final String name;

    public String getName() {
        return name;
    }

    public AddUserMessage(String name) {
        this.name = name;
    }

    @Override
    public MessageId getSerializationId() {
        return MessageId.ADD_USER;
    }
}
