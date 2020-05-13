package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.view.events.ClientEventsListener;

public class ExecuteActionMessage implements ClientMessage {
    private final User user;
    private final int id;
    private final ActionIdentifier actionIdentifier;
    private final Coordinate coordinate;

    public ExecuteActionMessage(User user, int id, ActionIdentifier actionIdentifier, Coordinate coordinate) {
        this.user = user;
        this.id = id;
        this.actionIdentifier = actionIdentifier;
        this.coordinate = coordinate;
    }

    public User getUser() {
        return user;
    }

    public int getId() {
        return id;
    }

    public ActionIdentifier getActionIdentifier() {
        return actionIdentifier;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public MessageId getSerializationId() {
        return MessageId.EXECUTE_ACTION;
    }

    @Override
    public boolean visit(ClientEventsListener listener) {
        return listener.onExecuteAction(user, id, actionIdentifier, coordinate);
    }
}

