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
    private static boolean move(Board b, Pawn p, Coordinate c) throws InvalidActionException {
        b.movePawn(p,c);
        return false;
    }
    private static boolean canMove(Board b, Pawn p, Coordinate c) {
        return isOnBoard(c) && !isOccupied(b,c) && c.isNeighbour(p.getPosition()) &&
                b.getBuildingAt(c).getLevelDifference(b.getBuildingAt(p.getPosition())) <= 0;
    }

    // MOVE_UP
    private static boolean moveUp(Board b, Pawn p, Coordinate c) throws InvalidActionException {
        b.movePawn(p,c);
        return b.getBuildingAt(c).getLevel() == BuildingLevel.LEVEL3;
    }
    private static boolean canMoveUp(Board b, Pawn p, Coordinate c) {
        return isOnBoard(c) && !isOccupied(b,c) && c.isNeighbour(p.getPosition()) &&
            b.getBuildingAt(p.getPosition()).getLevelDifference(b.getBuildingAt(c)) == -1;
    }

    //BUILD_BLOCK
    private static boolean buildBlock(Board b, Pawn p, Coordinate c) throws InvalidActionException {
        b.buildBlock(c);
        return false;
    }
    private static boolean canBuildBlock(Board b, Pawn p, Coordinate c) {
        return (!isOccupied(b, c) &&
            c.isNeighbour(p.getPosition()) &&
            b.getBuildingAt(c).getLevel() != BuildingLevel.LEVEL3 &&
            !b.getBuildingAt(c).hasDome());
    }

    //BUILD_DOME
    private static boolean buildDome(Board b, Pawn p, Coordinate c) throws InvalidActionException {
        b.buildDome(c);
        return false;
    }
    private static boolean canBuildDome(Board b, Pawn p, Coordinate c) {
        return (!b.getPawnAt(c).isPresent() && isOnBoard(c) &&
            c.isNeighbour(p.getPosition()) &&
            b.getBuildingAt(c).getLevel() == BuildingLevel.LEVEL3 &&
            !b.getBuildingAt(c).hasDome());
    }

    // PUSH PAWNS
    private static boolean pushPawn(Board b, Pawn p, Coordinate c) throws InvalidActionException {
        //push if opposite pawn is present
        if (b.getPawnAt(c).isPresent()){
            b.movePawn(b.getPawnAt(c).get(),
                    new Coordinate(c.getX() - p.getPosition().getX(), c.getY() - p.getPosition().getY()));
        }
        b.movePawn(p,c);
        return false;
    }
    private static boolean canPushPawn(Board b, Pawn p, Coordinate c){
        // destination = coordinate of the pushed pawn
        Coordinate destination = new Coordinate(c.getX() + (c.getX() - p.getPosition().getX()),
                c.getY() + (c.getY() - p.getPosition().getY()));
        return c.isNeighbour(p.getPosition()) && isOnBoard(c) && isOnBoard(destination) &&
                !isOccupied(b,destination) && !b.getBuildingAt(destination).hasDome() &&
                b.getBuildingAt(p.getPosition()).getLevelDifference(b.getBuildingAt(c)) <= 1;

    }

    //SWAP PAWNS
    //p1 is the pawn of the player who wants to swap, p2 is the pawn of the enemy
    private static boolean swapPawn(Board b, Pawn p1, Pawn p2) throws InvalidActionException {
        //swap if opposite pawn is present
        Coordinate tempCoordinate = p1.getPosition();
        b.movePawn(p1, p2.getPosition());
        b.movePawn(p2, tempCoordinate);
        return false;
    }
    private static boolean canSwapPawn(Board b, Pawn p1, Pawn p2){
        return p1.getPosition().isNeighbour(p2.getPosition()) &&
                b.getBuildingAt(p1.getPosition()).getLevelDifference(b.getBuildingAt(p2.getPosition())) <= 1;
    }


    private static boolean isOnBoard(Coordinate c){
        return ((c.getX() >= 0) && (c.getX() < 5)) && ((c.getY() >= 0) && (c.getY() < 5));
    }
    private static boolean isOccupied(Board b, Coordinate c){
        return b.getPawnAt(c).isPresent();
    }
}
