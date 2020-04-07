package it.polimi.ingsw.model.player.turnsequence;

import it.polimi.ingsw.model.action.Action;

import java.util.Collections;
import java.util.List;

public class DefaultTurnSequence implements TurnSequence {

    Action[] actions = new Action[2];

    private State state = State.FIRST;
    private enum State {
        FIRST,
        SECOND,
        END
    }

    @Override
    public List<Action> getStep() {
        switch (state){
            case FIRST:
                return Collections.singletonList(actions[0]);
            case SECOND:
                return Collections.singletonList(actions[1]);
            case END:
                return Collections.singletonList(Action.endTurn);
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public void nextStep(Action executedAction) {
        switch (state){
            case FIRST:
                if(executedAction.equals(actions[0])) {
                    state = State.SECOND;
                    break;
                }else {
                    throw new IllegalStateException();
                }
            case SECOND:
                if(executedAction.equals(actions[1])) {
                    state = State.END;
                    break;
                }else {
                    throw new IllegalStateException();
                }
            case END:
                if(!executedAction.equals(Action.endTurn)) {
                    throw new IllegalStateException();
                }
                break;
        }
    }

    @Override
    public void start() {
        state = State.FIRST;
    }

    public DefaultTurnSequence(Action firstAction, Action secondAction) {
        actions[0] = firstAction;
        actions[1] = secondAction;
    }
}
