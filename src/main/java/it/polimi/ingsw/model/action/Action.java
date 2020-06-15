package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.model.board.InvalidActionException;
import it.polimi.ingsw.model.player.Pawn;

import java.util.Arrays;

/**
 * Action that can be executed by a {@link Pawn} on the {@link Board}
 * Composed of a combination of {@link Checks} and {@link Effects}
 * @see Check
 * @see Effect
 */
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

    /**
     * Execute the action and modify the board
     * @param board Board
     * @param pawn Acting pawn
     * @param coordinate Target coordinate for the action
     * @return true if the action results in a win for the player that executed it, false otherwise
     * @throws InvalidActionException if executing the action results in illegal behaviour on the board
     */
    public boolean execute(Board board, Pawn pawn, Coordinate coordinate) throws InvalidActionException {
        boolean win = false;
        for (Effect effect : effects) {
            if (effect.execute(board, pawn, coordinate)){
                win = true;
            }
        }
        return win;
    }

    /**
     * Checks if the action is valid and could be executed
     * @param board Board
     * @param pawn Acting pawn
     * @param coordinate Target coordinate for the action
     * @return true if the action is valid, false otherwise
     */
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
        return family + ":" + description;
    }
}
