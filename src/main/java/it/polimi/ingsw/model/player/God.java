package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.action.ActionKind;

import java.lang.UnsupportedOperationException;

import java.util.Map;

public class God {
    private String name;
    private Map<ActionKind, Action> actionMap;
    private TurnSequence turnSequence;

    public Pawn createPawn() {
        throw new UnsupportedOperationException();
    }

    public TurnSequence getTurnSequence() {throw new UnsupportedOperationException();}

    @Override
    public String toString() {
        return name;
    }
}
