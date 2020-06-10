package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.player.Pawn;

import java.util.Arrays;

/**
 * The Action class represents the abstraction of an Action in the game, each action
 * can have multiple effects and restrictions (the restrictions are represented with
 * the Check class), each action also have a unique description that synthesizes the
 * effects of the action
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
     * Execute the specified Action on a given Coordinate
     * @param board The board on which the action is executed
     * @param pawn The pawn selected by the player that executes this action
     * @param coordinate The coordinate where the player wants to execute this action
     * @return true if the player that executes this action wins by executing it
     * @throws InvalidActionException if the chosen action can't be executed
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
     * Checks if the action matches all its checks on the given set of board,pawn,coordinate
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
