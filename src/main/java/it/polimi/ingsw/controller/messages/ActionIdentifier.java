package it.polimi.ingsw.controller.messages;

import it.polimi.ingsw.model.action.Action;

public class ActionIdentifier {
    private final String description;

    public String getDescription() {
        return description;
    }

    public boolean matches(Action action) {
        return description.equals(action.getDescription());
    }

    public ActionIdentifier(Action action) {
        description = action.getDescription();
    }
}
