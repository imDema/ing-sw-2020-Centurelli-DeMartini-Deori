package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.player.Pawn;

import java.util.ArrayList;
import java.util.List;


public class Action {
    private String description;
    private List<Effect> effects;
    private List<CheckAllowed> checkAllowed;

    public String getDescription() {
        return description;
    }

    public void execute(Board board, Pawn pawn, Coordinate coordinate) throws InvalidActionException {
        for (Effect lambda : effects) {
            lambda.execute(board,pawn,coordinate);
        }
    }

    /// Checks if all conditions are verified
    public boolean checkAllowed(Board board, Pawn pawn, Coordinate coordinate) {
        boolean flag = true;
        for (CheckAllowed lambda : checkAllowed) {
            if (!lambda.isAllowed(board, pawn, coordinate))
            {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public static final Action start = new Action("Start turn", new ArrayList<>(), new ArrayList<>());
    public static final Action endTurn = new Action("End turn", new ArrayList<>(), new ArrayList<>());

    public Action(String description, List<Effect> effect, List<CheckAllowed> checkAllowed) {
        this.description = description;
        this.effects = effect;
        this.checkAllowed = checkAllowed;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != Action.class)
            return false;
        Action a = (Action) obj;
        for(int i = 0, s = effects.size(); i < s; i++) {
            if(!a.effects.get(i).equals(effects.get(i)))
                return false;
        }
        for(int i = 0, s = checkAllowed.size(); i < s; i++) {
            if(!a.checkAllowed.get(i).equals(checkAllowed.get(i)))
                return false;
        }
        return true;
    }

    //
    // Default action's static methods
    //

    // MOVE
    private static boolean defaultMove(Board b, Pawn p, Coordinate c) throws InvalidActionException {
        b.movePawn(p,c);
        return false;
    }
    private static boolean defaultCheckMove(Board b, Pawn p, Coordinate c) {
        return !b.getPawnAt(c).isPresent() &&
                c.isNeighbour(p.getPosition()) &&
                b.getBuildingAt(p.getPosition())
                        .getLevelDifference(b.getBuildingAt(c)) >= 0;
    }

    // MOVE_UP
    private static boolean defaultMoveUp(Board b, Pawn p, Coordinate c) throws InvalidActionException {
        b.movePawn(p,c);
        if (b.getBuildingAt(c).getLevel() == BuildingLevel.LEVEL3) {return true;}
        else {return false;}
    }
    private static boolean defaultCheckMoveUp(Board b, Pawn p, Coordinate c) {
        return (!b.getPawnAt(c).isPresent() &&
            c.isNeighbour(p.getPosition()) &&
            b.getBuildingAt(p.getPosition()).getLevelDifference(b.getBuildingAt(c)) == -1);
    }

    // BUILD_BLOCK
    private static boolean defaultBuildBlock(Board b, Pawn p, Coordinate c) throws InvalidActionException {
        b.buildBlock(c);
        return false;
    }
    private static boolean defaultCheckBuildBlock(Board b, Pawn p, Coordinate c) {
        return (!b.getPawnAt(c).isPresent() &&
            c.isNeighbour(p.getPosition()) &&
            b.getBuildingAt(c).getLevel() != BuildingLevel.LEVEL3 &&
            !b.getBuildingAt(c).hasDome());

    }

    // BUILD_DOME
    private static boolean defaultBuildDome(Board b, Pawn p, Coordinate c) throws InvalidActionException {
        b.buildDome(c);
        return false;
    }
    private static boolean defaultCheckBuildDome(Board b, Pawn p, Coordinate c) {
        return (!b.getPawnAt(c).isPresent() &&
            c.isNeighbour(p.getPosition()) &&
            b.getBuildingAt(c).getLevel() == BuildingLevel.LEVEL3 &&
            !b.getBuildingAt(c).hasDome());

    }

    // END_TURN
    // Commented because not definitive
    //private static boolean defaultEndTurn(Game g, Board b, Pawn p, Coordinate c) {
    //   g.nextTurn();
    //    return false;
    //}
    //private static boolean defaultCheckEndTurn(Board b, Pawn p, Coordinate c) {
    //    return false;
    //}


//    //Custom action
//    public Action(Effect effect, CheckAllowed checkAllowed) {
//        this.effect = effect;
//        this.checkAllowed = checkAllowed;
//    }
}
