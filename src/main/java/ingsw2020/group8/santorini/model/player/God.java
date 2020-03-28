package ingsw2020.group8.santorini.model.player;

import ingsw2020.group8.santorini.model.action.Action;
import ingsw2020.group8.santorini.model.action.ActionKind;
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
