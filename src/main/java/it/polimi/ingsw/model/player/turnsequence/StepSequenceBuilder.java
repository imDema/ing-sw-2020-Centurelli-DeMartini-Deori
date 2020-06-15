package it.polimi.ingsw.model.player.turnsequence;

import it.polimi.ingsw.model.action.Action;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder class for {@link StepSequence}
 */
public class StepSequenceBuilder {
    private final List<Action[]> sequence = new ArrayList<>();

    /**
     * Add a step to the tail of the sequence
     * @param actions step to add
     * @return this
     */
    public StepSequenceBuilder addStep(Action[] actions) {
        sequence.add(actions);
        return this;
    }

    /**
     * Build the {@link StepSequence} and return it
     * @return The built StepSequence
     */
    public StepSequence build() {
        Action[][] array = new Action[sequence.size()][];
        for (int i = 0; i < sequence.size(); i++) {
            array[i] = sequence.get(i);
        }

        return new StepSequence(array);
    }
}
