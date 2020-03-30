package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.player.Pawn;

import java.lang.UnsupportedOperationException;


public class Action {
    private Effect effect;
    private CheckAllowed checkAllowed;

    public boolean execute(Board board, Pawn pawn, Coordinate coordinate) throws InvalidMoveException, InvalidBuildException {
        return effect.execute(board, pawn, coordinate);
    }

    public boolean checkAllowed(Board board, Pawn pawn, Coordinate coordinate) {
        return checkAllowed.isAllowed(board, pawn, coordinate);
    }

    //Create default action
    public Action(ActionKind actionKind) throws InvalidMoveException{
        switch (actionKind) {
            case MOVE:
                effect = Action::defaultMove;
                checkAllowed = Action::defaultCheckMove;
                break;
            case MOVE_UP:
                effect = Action::defaultMoveUp;
                checkAllowed = Action::defaultCheckMoveUp;
                break;
            case BUILD_BLOCK:
                effect = Action::defaultBuildBlock;
                checkAllowed = Action::defaultCheckBuildBlock;
                break;
            case BUILD_DOME:
                effect = Action::defaultBuildDome;
                checkAllowed = Action::defaultCheckBuildDome;
                break;
            case END_TURN:
                //effect = Action::defaultEndTurn;
                //checkAllowed = Action::defaultCheckEndTurn;
                break;
            default:
                break;
        }
    }


    //
    // Default action's static methods
    //

    // MOVE
    private static boolean defaultMove(Board b, Pawn p, Coordinate c) throws InvalidMoveException {
        b.movePawn(p,c);
        return false;
    }
    private static boolean defaultCheckMove(Board b, Pawn p, Coordinate c) {
        return !b.getPawnAt(c).isPresent() &&
                c.isNeighbour(b.getPawnPosition(p)) &&
                b.getBuildingAt(b.getPawnPosition(p))
                        .getLevelDifference(b.getBuildingAt(c)) >= 0;
    }

    // MOVE_UP
    private static boolean defaultMoveUp(Board b, Pawn p, Coordinate c) throws InvalidMoveException {
        b.movePawn(p,c);
        if (b.getBuildingAt(c).getLevel() == BuildingLevel.LEVEL3) {return true;}
        else {return false;}
    }
    private static boolean defaultCheckMoveUp(Board b, Pawn p, Coordinate c) {
        return (!b.getPawnAt(c).isPresent() &&
            c.isNeighbour(b.getPawnPosition(p)) &&
            b.getBuildingAt(b.getPawnPosition(p)).getLevelDifference(b.getBuildingAt(c)) == -1);
    }

    // BUILD_BLOCK
    private static boolean defaultBuildBlock(Board b, Pawn p, Coordinate c) throws InvalidBuildException {
        b.buildBlock(c);
        return false;
    }
    private static boolean defaultCheckBuildBlock(Board b, Pawn p, Coordinate c) {
        return (!b.getPawnAt(c).isPresent() &&
            c.isNeighbour(b.getPawnPosition(p)) &&
            b.getBuildingAt(c).getLevel() != BuildingLevel.LEVEL3 &&
            !b.getBuildingAt(c).hasDome());

    }

    // BUILD_DOME
    private static boolean defaultBuildDome(Board b, Pawn p, Coordinate c) throws InvalidBuildException {
        b.buildDome(c);
        return false;
    }
    private static boolean defaultCheckBuildDome(Board b, Pawn p, Coordinate c) {
        return (!b.getPawnAt(c).isPresent() &&
            c.isNeighbour(b.getPawnPosition(p)) &&
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


    //Custom action
    public Action(Effect effect, CheckAllowed checkAllowed) {
        this.effect = effect;
        this.checkAllowed = checkAllowed;
    }
}
