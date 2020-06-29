package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.BuildingLevel;
import it.polimi.ingsw.model.board.Coordinate;

public abstract class Checks {
    /**
     * Target coordinate is not occupied by any pawn
     */
    public static final Check notOccupied = (b, p, c) -> b.getPawnAt(c).isEmpty();

    /**
     * Target coordinate is not occupied by any pawn or is the coordinate the pawn is standing on
     */
    public static final Check notOccupiedOrSelf = (b, p, c) -> b.getPawnAt(c).map(p::equals).orElse(true);

    /**
     * Target coordinate is not occupied by an ally pawn
     */
    public static final Check noAlly = (b, p, c) ->
            b.getPawnAt(c).map(p2 -> !p2.getOwner().equals(p.getOwner())).orElse(true);

    /**
     * Target coordinate is neighbouring the pawn position
     */
    public static final Check neighbour = (b, p, c) -> c.isNeighbour(p.getPosition());

    /**
     * Target coordinate is neighbouring the pawn position considering opposing edges and corners adjacent
     */
    public static final Check neighbourLooping = (b, p, c) -> {
        Coordinate c1 = p.getPosition();
        int dx = Math.abs(c1.getX() - c.getX());
        int dy = Math.abs(c1.getY() - c.getY());
        return (dx <= 1 || dx == Board.BOARD_SIZE - 1) &&
                (dy <= 1 || dy == Board.BOARD_SIZE - 1);
    };

    /**
     * The building at the target coordinate is at most one level higher than that
     * at the pawn's coordinate
     */
    public static final Check maxOneLevelAbove = (b, p, c) -> {
        Coordinate pawnCoordinate = p.getPosition();
        return b.getBuildingAt(pawnCoordinate).getLevelDifference(b.getBuildingAt(c)) <= 1;
    };
    /**
     * The building at the target coordinate is at the same level or lower than that
     * at the pawn's coordinate
     */
    public static final Check maxSameLevel = (b, p, c) -> {
        Coordinate pawnCoordinate = p.getPosition();
        return b.getBuildingAt(pawnCoordinate).getLevelDifference(b.getBuildingAt(c)) <= 0;
    };

    /**
     * The building at the target coordinate does not have a dome
     */
    public static final Check noDome = (b, p, c) -> !b.getBuildingAt(c).hasDome();

    /**
     * The building at the target coordinate is level 3
     */
    public static final Check maxLevel = (b, p, c) -> b.getBuildingAt(c).getLevel() == BuildingLevel.LEVEL3;

    /**
     * The building at the target coordinate is not level 3
     */
    public static final Check notMaxLevel = (b, p, c) -> b.getBuildingAt(c).getLevel() != BuildingLevel.LEVEL3;

    /**
     * The building at the target coordinate is level 1 or higher
     */
    public static final Check minLevelOne = (b, p, c) -> b.getBuildingAt(c).getLevel() != BuildingLevel.LEVEL0;

    public static final Check notPerimeter = (b, p, c) ->
            c.getX() != 0 && c.getX() != Board.BOARD_SIZE - 1 &&
            c.getY() != 0 && c.getY() != Board.BOARD_SIZE - 1;

    /**
     * If there is a pawn at the target coordinate check if it could be pushed one tile along the pawn to target
     * coordinate direction
     */
    public static final Check canPush = (b, p, c) -> b.getPawnAt(c).map(p2 -> {
        //Push destination
        int x1 = c.getX() + (c.getX() - p.getPosition().getX());
        int y1 = c.getY() + (c.getY() - p.getPosition().getY());
        Coordinate destination = new Coordinate(x1, y1);

        return b.isOnBoard(destination) &&
                notOccupied.isAllowed(b, null, destination) &&
                noDome.isAllowed(b, null, destination);
    }).orElse(true);

    /**
     * There is a pawn on the target coordinate and it can be forced on the opposing coordinate relative to the
     * acting pawn
     */
    public static final Check canFerry = (b, p, c) -> {
        Coordinate selfCoordinate = p.getPosition();
        int x1 = selfCoordinate.getX() + (selfCoordinate.getX() - c.getX());
        int y1 = selfCoordinate.getY() + (selfCoordinate.getY() - c.getY());
        Coordinate destination = new Coordinate(x1, y1);

        return b.isOnBoard(destination) &&
                notOccupied.isAllowed(b, null, destination) &&
                !notOccupied.isAllowed(b, null, c) &&
                noDome.isAllowed(b, null, destination);
    };
}
