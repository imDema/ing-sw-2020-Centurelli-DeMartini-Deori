package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.messages.User;

public class ChooseFirstPlayerMessage implements Message {
    private final User self;
    private final User firstPlayer;

    public ChooseFirstPlayerMessage(User self, User firstPlayer) {
        this.self = self;
        this.firstPlayer = firstPlayer;
    }

    @Override
    public MessageId getSerializationId() {
        return MessageId.CHOOSE_FIRST_PLAYER;
    }

    @Override
    public boolean visit(MessageDispatcher dispatcher) {
        return dispatcher.onChooseFirstPlayer(self, firstPlayer);
    }
}
