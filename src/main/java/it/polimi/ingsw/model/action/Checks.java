package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.board.BuildingLevel;
import it.polimi.ingsw.model.board.Coordinate;

public abstract class Checks {
    /**
     * Target coordinate is not occupied by any pawn
     */
    public static final Check notOccupied = (b, p, c) -> b.getPawnAt(c).isEmpty();

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
}
