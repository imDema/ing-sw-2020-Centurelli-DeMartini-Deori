package it.polimi.ingsw.controller.messages;

import it.polimi.ingsw.model.player.God;

public class GodIdentifier {
    private final String name;
    private final String description;

    public GodIdentifier(God god) {
        this.name = god.getName();
        this.description = god.getDescription();
    }

    public boolean matches(God god) {
        return name.equals(god.getName()) && description.equals(god.getDescription());
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
