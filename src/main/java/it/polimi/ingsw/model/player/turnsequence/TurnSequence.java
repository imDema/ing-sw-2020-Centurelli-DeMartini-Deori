package it.polimi.ingsw.model.player.turnsequence;

import it.polimi.ingsw.model.action.Action;

import java.util.List;

public interface TurnSequence {
    List<Action> getStep();   //Get allowed actions in this phase of the turn
    void nextStep(Action executedAction);
    void start();
}
