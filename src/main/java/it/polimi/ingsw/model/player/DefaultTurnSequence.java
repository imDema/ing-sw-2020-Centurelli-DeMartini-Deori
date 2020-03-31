package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.action.ActionKind;

import java.util.List;

public class DefaultTurnSequence implements TurnSequence {

    private enum StepCounter{
        MOVE, BUILD, END_TURN
    }

    private StepCounter stepCounter = StepCounter.MOVE;

    @Override
    public List<ActionKind> getStep() {
        switch (stepCounter){
            case MOVE:
                return List.of(new ActionKind[]{ActionKind.MOVE, ActionKind.MOVE_UP});
            case BUILD:
                return List.of(new ActionKind[]{ActionKind.BUILD_BLOCK, ActionKind.BUILD_DOME});
            case END_TURN:
                return List.of(new ActionKind[]{ActionKind.END_TURN});
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public void nextStep(ActionKind executedAction) {
        switch (stepCounter){
            case MOVE:
                if(executedAction.equals(ActionKind.MOVE) || executedAction.equals(ActionKind.MOVE_UP)) {
                    stepCounter = StepCounter.BUILD;
                    break;
                }else {
                    throw new IllegalStateException();
                }
            case BUILD:
                if(executedAction.equals(ActionKind.BUILD_BLOCK) || executedAction.equals(ActionKind.BUILD_DOME)) {
                    stepCounter = StepCounter.END_TURN;
                    break;
                }else {
                    throw new IllegalStateException();
                }
            case END_TURN:
                break;
        }
    }

    @Override
    public void start() {
        stepCounter = StepCounter.MOVE;
    }
}
