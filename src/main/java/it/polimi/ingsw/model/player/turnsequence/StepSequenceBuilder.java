package it.polimi.ingsw.model.player.turnsequence;

import it.polimi.ingsw.model.action.Action;

import java.util.ArrayList;
import java.util.List;

// Mainly for testing purposes
public class StepSequenceBuilder {
    private List<Action[]> sequence = new ArrayList<>();
    public StepSequenceBuilder addStep(Action[] actions) {
        sequence.add(actions);
        return this;
    }
    public StepSequence build() {
        Action[][] array = new Action[sequence.size()][];
        for (int i = 0; i < sequence.size(); i++) {
            array[i] = sequence.get(i);
        }

        return new StepSequence(array);
    }
}
