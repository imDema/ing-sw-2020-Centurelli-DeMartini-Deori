package it.polimi.ingsw.model.player.turnsequence;

import it.polimi.ingsw.model.action.Action;

import java.util.Arrays;
import java.util.Optional;

/**
 * The BranchingTurnSequence is a TurnSequence where the choice of the next step of the sequence isn't
 * necessarily a deterministic one, each step of the sequence can have multiple next steps,
 * this kind of TurnSequence contains branches
 */
public class BranchingTurnSequence implements TurnSequence {
    private final StepSequence commonSteps;
    private final StepSequence[] branches;
    private Action[] currentStep = new Action[]{};
    private int branch = 0;
    private State state = State.COMMON;

    public BranchingTurnSequence(StepSequence commonSteps, StepSequence firstBranch, StepSequence secondBranch) {
        this.commonSteps = commonSteps;
        this.branches = new StepSequence[] {firstBranch, secondBranch};
    }

    private static Action[] concatArrays(Action[] a, Action[] b) {
        Action[] ret = new Action[a.length + b.length];
        System.arraycopy(a, 0, ret, 0, a.length);
        System.arraycopy(b, 0, ret, a.length, b.length);
        return ret;
    }

    @Override
    public Action[] getStep() {
        return currentStep;
    }

    /**
     * Go to the next steps of the sequence based on the last executed action,
     * if the next step is a branch both of the choices will be available, if the
     * executed action went into a specific branch the next step of that branch
     * will be available
     * @param executedAction is the step of the sequence that was executed
     */
    @Override
    public void nextStep(Action executedAction) {
        if(!Arrays.asList(currentStep).contains(executedAction))
            throw new IllegalStateException();

        Optional<Action[]> common = commonSteps.next();
        if (common.isPresent()) {
            currentStep = common.get();
        } else {
            switch (state) {
                case COMMON:
                    // Return both branches' first step
                    Action[] b0 = branches[0].peek().orElse(new Action[0]);
                    Action[] b1 = branches[1].peek().orElse(new Action[0]);
                    currentStep = concatArrays(b0, b1);
                    state = State.BRANCHING;
                break;
                case BRANCHING:
                    // Choose the branch depending on which action was taken
                    Action[] first = branches[0].next().orElse(new Action[0]);
                    branches[1].next();
                    if (Arrays.asList(first).contains(executedAction)) {
                        branch = 0;
                    } else {
                        branch = 1;
                    }
                    state = State.BRANCHED;
                case BRANCHED:
                    // Return step from chosen branch
                    currentStep = branches[branch].next()
                            .orElse(new Action[] {Action.endTurn});
                break;
            }
        }
    }

    /**
     * Starts the sequence, if there aren't common steps and all branches
     * are empty the method ends the turn and return
     */
    @Override
    public void start() {
        commonSteps.start();
        branches[0].start();
        branches[1].start();

        Optional<Action[]> common = commonSteps.next();
        if (common.isPresent()) {
            state = State.COMMON;
            currentStep = common.get();
        } else {
            // If there are no common steps immediately start branching
            Action[] b0 = branches[0].peek().orElse(new Action[0]);
            Action[] b1 = branches[1].peek().orElse(new Action[0]);
            Action[] actions = concatArrays(b0, b1);

            if (actions.length > 0) {
                state = State.BRANCHING;
                currentStep = actions;
            } else {
                // All branches are empty, return end turn
                state = State.BRANCHED;
                currentStep = new Action[] {Action.endTurn};
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BranchingTurnSequence) {
            BranchingTurnSequence o = (BranchingTurnSequence) obj;
            return commonSteps.equals(o.commonSteps) &&
                    branches[0].equals(o.branches[0]) &&
                    branches[1].equals(o.branches[1]);
        }
        return false;
    }

    enum State {
        COMMON,
        BRANCHING,
        BRANCHED
    }

    @Override
    public String toString() {
        return "BranchingTurnSequence{" +
                "commonSteps:" + commonSteps +
                ", branch[0]:" + branches[0] +
                ", branch[1]:" + branches[1] +
                "}";
    }
}
