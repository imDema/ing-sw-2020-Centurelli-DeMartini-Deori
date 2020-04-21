package it.polimi.ingsw.model.player.turnsequence;

import it.polimi.ingsw.model.action.Action;

import java.util.Arrays;

public class BranchingTurnSequence implements TurnSequence {
    private final StepSequence commonSteps;
    private final StepSequence firstBranch;
    private final StepSequence secondBranch;
    private Action[] currentStep = new Action[]{};
    private Action[] firstBranchDiscriminator;
    private Action[] secondBranchDiscriminator;
    private boolean branching = false;
    private boolean onFirstBranch = true;

    public BranchingTurnSequence(StepSequence commonSteps, StepSequence firstBranch, StepSequence secondBranch) {
        this.commonSteps = commonSteps;
        this.firstBranch = firstBranch;
        this.secondBranch = secondBranch;
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

    @Override
    public void nextStep(Action executedAction) {
        if (branching) {

        }
        currentStep = commonSteps.next()
                .orElse(concatArrays(firstBranchDiscriminator, secondBranchDiscriminator));
    }

    @Override
    public void start() {
        commonSteps.start();
        firstBranch.start();
        secondBranch.start();
        firstBranchDiscriminator = firstBranch.next().orElse(new Action[]{});
        secondBranchDiscriminator = secondBranch.next().orElse(new Action[]{});


    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BranchingTurnSequence) {
            BranchingTurnSequence o = (BranchingTurnSequence) obj;
            return commonSteps.equals(o.commonSteps) &&
                    firstBranch.equals(o.firstBranch) &&
                    secondBranch.equals(o.secondBranch);
        }
        return false;
    }

    @Override
    public String toString() {
        return "DefaultTurnSequence{" +
                "commonSteps:" + commonSteps +
                ", firstBranch:" + firstBranch +
                ", secondBranch:" + secondBranch +
                "}";
    }
}
