package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.player.Pawn;

import java.util.Arrays;

public class Action {
    public static final Action start = new Action("Start turn", ActionFamily.NONE, new Effect[0], new Check[0]);
    public static final Action endTurn = new Action("End turn", ActionFamily.NONE, new Effect[0], new Check[0]);
    private final ActionFamily family;
    private final String description;
    private final Effect[] effects;
    private final Check[] checks;

    public Action(String description, ActionFamily family, Effect[] effect, Check[] checks) {
        this.description = description;
        this.family = family;
        this.effects = effect;
        this.checks = checks;
    }

    public ActionFamily getFamily() {
        return family;
    }

    public String getDescription() {
        return description;
    }

    public void execute(Board board, Pawn pawn, Coordinate coordinate) throws InvalidActionException {
        for (Effect effect : effects) {
            effect.execute(board, pawn, coordinate);
        }
    }

    /// Checks if all conditions are verified
    public boolean checkAllowed(Board board, Pawn pawn, Coordinate coordinate) {
        return Arrays.stream(checks).allMatch(l -> l.isAllowed(board,pawn,coordinate));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != Action.class)
            return false;
        Action a = (Action) obj;
        if (!description.equals(a.description))
            return false;

        for(int i = 0, s = effects.length; i < s; i++) {
            if(!a.effects[i].equals(effects[i]))
                return false;
        }
        for(int i = 0, s = checks.length; i < s; i++) {
            if(!a.checks[i].equals(checks[i]))
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return description;
    }
}
