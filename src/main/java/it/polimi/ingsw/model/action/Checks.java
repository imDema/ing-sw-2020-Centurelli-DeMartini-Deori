package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.board.BuildingLevel;
import it.polimi.ingsw.model.board.Coordinate;

public abstract class Checks {
    public static final Check notOccupied = (b, p, c) -> b.getPawnAt(c).isEmpty();

    public static final Check noAlly = (b, p, c) ->
            b.getPawnAt(c).map(p2 -> !p2.getOwner().equals(p.getOwner())).orElse(true);

    public static final Check neighbour = (b, p, c) -> c.isNeighbour(p.getPosition());

    public static final Check maxOneLevelAbove = (b, p, c) -> {
        Coordinate pawnCoordinate = p.getPosition();
        return b.getBuildingAt(pawnCoordinate).getLevelDifference(b.getBuildingAt(c)) <= 1;
    };

    public static final Check maxSameLevel = (b, p, c) -> {
        Coordinate pawnCoordinate = p.getPosition();
        return b.getBuildingAt(pawnCoordinate).getLevelDifference(b.getBuildingAt(c)) <= 0;
    };

    public static final Check noDome = (b, p, c) -> !b.getBuildingAt(c).hasDome();

    public static final Check maxLevel = (b, p, c) -> b.getBuildingAt(c).getLevel() == BuildingLevel.LEVEL3;

    public static final Check notMaxLevel = (b, p, c) -> b.getBuildingAt(c).getLevel() != BuildingLevel.LEVEL3;

    public static final Check minLevelOne = (b, p, c) -> b.getBuildingAt(c).getLevel() != BuildingLevel.LEVEL0;

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
