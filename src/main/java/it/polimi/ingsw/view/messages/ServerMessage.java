package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.controller.events.ServerEventsListener;

public interface ServerMessage extends Message {
    void visit(ServerEventsListener listener);
}
