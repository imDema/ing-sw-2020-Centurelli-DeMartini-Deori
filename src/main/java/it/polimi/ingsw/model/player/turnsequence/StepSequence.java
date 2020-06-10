package it.polimi.ingsw.model.player.turnsequence;

import it.polimi.ingsw.model.action.Action;

import java.util.Arrays;
import java.util.Optional;

/**
 * The StepSequence class represent the abstraction of a sequence of Actions made by steps.
 * The sequence takes trace of the current step, it's the state of the sequence.
 * Given a state, the next step can be reached by calling the method next() if the state isn't the last step
 */
public class StepSequence {
    private int state = 0;
    private final Action[][] steps;

    public Optional<Action[]> next() {
        if (state < steps.length) {
            state += 1;
            return Optional.of(steps[state - 1]);
        } else {
            return Optional.empty();
        }
    }

    /**
     *
     * @return the Action that corresponds to the current state
     */
    public Optional<Action[]> peek() {
        if (state < steps.length) {
            return Optional.of(steps[state]);
        } else {
            return Optional.empty();
        }
    }

    public void start() {
        state = 0;
    }

    protected StepSequence(Action[][] steps) {
        this.steps = steps;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StepSequence) {
            StepSequence o = (StepSequence) obj;
            return state == o.state &&
                    Arrays.deepEquals(steps, o.steps);
        }
        return false;
    }

    @Override
    public String toString() {
        return "{steps:" + Arrays.deepToString(steps) +
                ", state:" + state +
                "}"
                ;
    }
}
