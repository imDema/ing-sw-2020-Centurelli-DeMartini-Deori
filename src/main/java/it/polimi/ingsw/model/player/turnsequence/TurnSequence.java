package it.polimi.ingsw.model.player.turnsequence;

import it.polimi.ingsw.model.action.Action;

/**
 * A player's sequence of steps during a turn.
 */
public interface TurnSequence {
    /**
     * Get the actions available to the player in this phase of the turn
     * @return array of available actions
     */
    Action[] getStep();

    /**
     * Advance the turn to the next phase.
     * @param executedAction The action that has been executed in the current phase
     */
    void nextStep(Action executedAction);
    void start();
}
