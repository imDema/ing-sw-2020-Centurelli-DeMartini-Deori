package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.player.turnsequence.TurnSequence;

public class God {
    private String name;
    private TurnSequence turnSequence;

    protected TurnSequence getTurnSequence() {
        return turnSequence;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public God(String name, TurnSequence turnSequence) {
        this.name = name;
        this.turnSequence = turnSequence;
    }
}
