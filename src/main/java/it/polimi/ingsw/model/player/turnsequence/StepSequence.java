package it.polimi.ingsw.model.player.turnsequence;

import it.polimi.ingsw.model.action.Action;

import java.util.Arrays;
import java.util.Optional;

/**
 * Iterator over an ordered list of steps.
 * A step is an {@code Action[]}
 */
public class StepSequence {
    private int state = 0;
    private final Action[][] steps;

    /**
     * Advance the iterator.
     * @return the next step
     */
    public Optional<Action[]> next() {
        if (state < steps.length) {
            state += 1;
            return Optional.of(steps[state - 1]);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Observe the current step without advancing the iterator.
     * @return Current step
     */
    public Optional<Action[]> peek() {
        if (state < steps.length) {
            return Optional.of(steps[state]);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Reset the iterator to its starting position.
     */
    public void start() {
        state = 0;
    }

    /**
     * @param steps ordered array of steps.
     */
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
