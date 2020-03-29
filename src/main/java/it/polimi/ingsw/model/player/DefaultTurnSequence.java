package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.action.ActionKind;

import java.util.List;

public class DefaultTurnSequence implements TurnSequence {
    @Override
    public List<ActionKind> getStep() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void nextStep(ActionKind executedAction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void start() {
        throw new UnsupportedOperationException();
    }
}
