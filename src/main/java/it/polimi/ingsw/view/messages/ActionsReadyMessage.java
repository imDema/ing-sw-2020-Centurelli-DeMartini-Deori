package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.User;

import java.util.List;

public class ActionsReadyMessage implements Message{
    private final User user;
    private final List<ActionIdentifier> actionIdentifiers;

    public User getUser() {
        return user;
    }

    public List<ActionIdentifier> getActionIdentifiers() {
        return actionIdentifiers;
    }

    public ActionsReadyMessage(User user, List<ActionIdentifier> actionIdentifiers) {
        this.user = user;
        this.actionIdentifiers = actionIdentifiers;
    }

    @Override
    public MessageId getSerializationId() {
        return MessageId.ACTION_READY;
    }
}
