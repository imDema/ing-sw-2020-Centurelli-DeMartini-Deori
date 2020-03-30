package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.action.ActionKind;
import it.polimi.ingsw.model.board.InvalidMoveException;

import java.lang.UnsupportedOperationException;

import java.util.HashMap;
import java.util.Map;

public class God {
    private String name;
    private Map<ActionKind, Action> actionMap = new HashMap<>();
    private TurnSequence turnSequence;

    public TurnSequence getTurnSequence() {
        return turnSequence;
    }

    public Action getAction(ActionKind actionKind) {
        return actionMap.get(actionKind);
    }

    public void setCustomAction(ActionKind actionKind, Action action) {
        actionMap.replace(actionKind, action);
    }

    @Override
    public String toString() {
        return name;
    }

    public God(String name) throws InvalidMoveException {
        this.name = name;

        // Load default actions
        actionMap.put(ActionKind.MOVE, new Action(ActionKind.MOVE));
        actionMap.put(ActionKind.MOVE_UP, new Action(ActionKind.MOVE_UP));
        actionMap.put(ActionKind.BUILD_BLOCK, new Action(ActionKind.BUILD_BLOCK));
        actionMap.put(ActionKind.BUILD_DOME, new Action(ActionKind.BUILD_DOME));
    }
}
