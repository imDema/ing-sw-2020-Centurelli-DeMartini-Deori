package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;

import static it.polimi.ingsw.view.messages.MessageId.CHOOSE_GOD;

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
        return CHOOSE_GOD;
    }

    @Override
    public boolean visit(MessageDispatcher dispatcher) {
        return dispatcher.onChooseGod(user, god);
    }
}
