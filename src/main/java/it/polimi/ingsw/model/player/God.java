package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.player.turnsequence.TurnSequence;

/**
 * The God class represents the abstraction of the God cards in the game,
 * each god has a name and a description, the description explains the custom
 * effects the god can have
 */
public class God {
    final private String name;
    final private String description;
    final private TurnSequence turnSequence;
    public God(String name, String description, TurnSequence turnSequence) {
        this.name = name;
        this.description = description;
        this.turnSequence = turnSequence;
    }

    public String getDescription() {
        return description;
    }

    protected TurnSequence getTurnSequence() {
        return turnSequence;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "God{name:\"" + name +
                "\", description:\"" + description +
                "\", turnSequence:" + turnSequence.toString() + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof God) {
            God g = (God) obj;
            return name.equals(g.name) &&
                    description.equals(g.description) &&
                turnSequence.equals(g.turnSequence);
        }
        return  false;
    }
}
