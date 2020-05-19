package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.messages.User;

import static it.polimi.ingsw.view.messages.MessageId.TURN_CHANGE;

public class TurnChangeMessage implements Message {
    private final User user;
    private final int turn;

    public TurnChangeMessage(User user, int turn) {
        this.user = user;
        this.turn = turn;
    }

    public User getUser() {
        return user;
    }

    public int getTurn() {
        return turn;
    }

    @Override
    public MessageId getSerializationId() {
        return TURN_CHANGE;
    }

    @Override
    public boolean visit(MessageDispatcher dispatcher) {
        return dispatcher.onTurnChange(user, turn);
    }
}
