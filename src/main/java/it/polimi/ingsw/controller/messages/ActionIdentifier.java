package it.polimi.ingsw.controller.messages;

import it.polimi.ingsw.model.action.Action;

/**
 * The ActionIdentifier class it's a 1:1 correspondence to an action.
 * This class allows to get a action without containing
 * all the information required for a real action
 */
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
