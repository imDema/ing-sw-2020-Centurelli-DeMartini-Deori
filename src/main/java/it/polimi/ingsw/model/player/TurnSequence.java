package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.action.ActionKind;

import java.util.List;

public interface TurnSequence {
    List<ActionKind> getStep();
    void nextStep(ActionKind executedAction);
    void start();
}
