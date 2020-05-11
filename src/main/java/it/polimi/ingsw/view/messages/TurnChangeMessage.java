package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.messages.User;

public class TurnChangeMessage implements Message{
    private User user;
    private int turn;

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
        return MessageId.TURN_CHANGE;
    }
}
