package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.view.events.ClientEventsListener;

public interface ClientMessage extends Message {
    boolean visit(ClientEventsListener listener);
}
