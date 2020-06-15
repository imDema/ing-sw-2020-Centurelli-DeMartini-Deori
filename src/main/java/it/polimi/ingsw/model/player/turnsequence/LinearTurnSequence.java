package it.polimi.ingsw.model.player.turnsequence;

import it.polimi.ingsw.model.action.Action;

import java.util.Arrays;

/**
 * {@link TurnSequence} that consist of a simple ordered list of steps.
 */
public class LinearTurnSequence implements TurnSequence {
    final StepSequence sequence;
    private Action[] currentStep = new Action[]{};

    @Override
    public Action[] getStep() {
        return currentStep;
    }

    @Override
    public void nextStep(Action executedAction) {
        if(!Arrays.asList(currentStep).contains(executedAction))
            throw new IllegalStateException();

        currentStep = sequence.next().orElse(new Action[]{Action.endTurn});
    }

    @Override
    public void start() {
        sequence.start();
        currentStep = sequence.next().orElse(new Action[]{Action.endTurn});
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LinearTurnSequence) {
            LinearTurnSequence o = (LinearTurnSequence) obj;
            return sequence.equals(o.sequence);
        }
        return false;
    }

    @Override
    public String toString() {
        return "LinearTurnSequence {sequence:" + sequence.toString() + "}";
    }

    public LinearTurnSequence(StepSequence sequence) {
        this.sequence = sequence;
    }
}
