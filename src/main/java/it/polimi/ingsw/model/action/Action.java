package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.player.Pawn;

public class Action {
    private final String description;
    private final Effect[] effects;
    private final Check[] checks;

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
        boolean flag = true;
        for (Check lambda : checks) {
            if (!lambda.isAllowed(board, pawn, coordinate))
            {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public static final Action start = new Action("Start turn", new Effect[0], new Check[0]);
    public static final Action endTurn = new Action("End turn", new Effect[0], new Check[0]);

    public Action(String description, Effect[] effect, Check[] checks) {
        this.description = description;
        this.effects = effect;
        this.checks = checks;
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
