package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.events.ServerEventsListener;
import it.polimi.ingsw.controller.messages.User;

public class TurnChangeMessage implements ServerMessage {
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
        return MessageId.TURN_CHANGE;
    }

    @Override
    public void visit(ServerEventsListener listener) {
        listener.onTurnChange(user, turn);
    }
}
