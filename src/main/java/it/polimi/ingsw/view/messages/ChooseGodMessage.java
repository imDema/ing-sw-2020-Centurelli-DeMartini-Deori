package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;

public class ChooseGodMessage implements Message {
    private final User user;
    private final GodIdentifier god;

    public ChooseGodMessage(User user, GodIdentifier god) {
        this.user = user;
        this.god = god;
    }

    public GodIdentifier getGod() {
        return god;
    }

    public User getUser() {
        return user;
    }

    @Override
    public MessageId getSerializationId() {
        return MessageId.CHOOSE_GOD;
    }
}
