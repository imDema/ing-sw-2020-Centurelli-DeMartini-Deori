package it.polimi.ingsw.model.player.turnsequence;

import it.polimi.ingsw.model.action.Action;

/**
 * The TurnSequence interface represents the abstraction of the Actions that a player
 * can execute during its turn, any action corresponds to a step in the sequence,
 * the turn progress in time when nextStep() its called.
 * Calling getStep() shows the available actions in the current phase of the turn
 */
public interface TurnSequence {
    Action[] getStep();
    void nextStep(Action executedAction);
    void start();
}
