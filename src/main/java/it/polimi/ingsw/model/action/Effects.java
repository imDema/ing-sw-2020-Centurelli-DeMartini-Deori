package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.board.Building;
import it.polimi.ingsw.model.board.BuildingLevel;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;

public abstract class Effects {
    /**
     * Move the pawn to the target coordinate
     * Winning effect if moving up to level 3
     */
    public static final Effect move = (b, p, c) -> {
        BuildingLevel oldLevel = b.getBuildingAt(p.getPosition()).getLevel();
        BuildingLevel newLevel = b.getBuildingAt(c).getLevel();

        b.movePawn(p,c);
        //Check win condition
        return oldLevel != BuildingLevel.LEVEL3 && newLevel == BuildingLevel.LEVEL3;
    };

    /**
     * Winning move if the building at the target coordinate is two or more levels lower than that at
     * the pawn's coordinate
     */
    public static final Effect winOnJumpDown = (b,p,c) -> {
        Building oldB = b.getBuildingAt(p.getPosition());
        Building newB = b.getBuildingAt(c);

        return oldB.getLevelDifference(newB) <= -2;
    };

    /**
     * Build a block at the target coordinate
     */
    public static final Effect buildBlock = (b, p, c) -> {
        b.buildBlock(c);
        return false;
    };

    /**
     * Build a dome at the target coordinate
     */
    public static final Effect buildDome = (b, p, c) -> {
        b.buildDome(c);
        return false;
    };

    /**
     * If there is a pawn at the target coordinate push it along the pawn position to target coordinate direction
     */
    public static final Effect pushPawn = (b, p, c) -> {
        if (b.getPawnAt(c).isPresent())
        {
            Pawn p2 = b.getPawnAt(c).get();
            int x1 = c.getX() + (c.getX() - p.getPosition().getX());
            int y1 = c.getY() + (c.getY() - p.getPosition().getY());
            Coordinate destination = new Coordinate(x1, y1);

            b.movePawn(p2, destination);
        }

        return false;
    };

    /**
     * If there is another pawn at the target coordinate swap its position with the pawn, otherwise
     * move the pawn to the target coordinate
     * Winning effect if moving up to level 3
     */
    public static final Effect swapPawns = (b, p, c) -> {
        BuildingLevel oldLevel = b.getBuildingAt(p.getPosition()).getLevel();
        BuildingLevel newLevel = b.getBuildingAt(c).getLevel();

        b.getPawnAt(c)
                .ifPresent(p2 -> b.swapPawn(p, p2));

        b.movePawn(p,c);

        //Check win condition
        return oldLevel != BuildingLevel.LEVEL3 && newLevel == BuildingLevel.LEVEL3;
    };

    public static final Effect ferry = (b, p, c) -> {
        Coordinate selfCoordinate = p.getPosition();
        int x1 = selfCoordinate.getX() + (selfCoordinate.getX() - c.getX());
        int y1 = selfCoordinate.getY() + (selfCoordinate.getY() - c.getY());
        Coordinate destination = new Coordinate(x1, y1);
        if (b.getPawnAt(c).isPresent()) {
            Pawn p2 = b.getPawnAt(c).get();
            b.movePawn(p2, destination);
        }
        return false;
    };

    /**
     * Adds a persistent effect that forbids executing ActionFamily.MOVE actions that target buildings
     * at a higher level for a number of turns equal to the number of players
     */
    public static final Effect forbidMoveUp = (board, pawn, coordinate) -> {
        Building oldB = board.getBuildingAt(pawn.getPosition());
        Building newB = board.getBuildingAt(coordinate);
        int levelDifference = oldB.getLevelDifference(newB);

        if (levelDifference == 1){
            final CheckEffect checkEffect = (b, p, c, a) ->
                    !(a.getFamily() == ActionFamily.MOVE &&
                            b.getBuildingAt(p.getPosition()).getLevelDifference(b.getBuildingAt(c)) > 0);

            int duration = board.countPawns() / 2;
            board.addCheckEffect(duration, checkEffect);
        }

        return false;
    };

    /**
     * Adds a persistent effect that forbids executing ActionFamily.MOVE actions that target buildings
     * at a lower level for a number of turns equal to the number of players
     */
    public static final Effect forbidMoveDown = (board, pawn, coordinate) -> {
        final CheckEffect checkEffect = (b, p, c, a) ->
            !(a.getFamily() == ActionFamily.MOVE &&
                    b.getBuildingAt(p.getPosition()).getLevelDifference(b.getBuildingAt(c)) < 0);

        int duration = board.countPawns() / 2;
        board.addCheckEffect(duration, checkEffect);

        return false;
    };

    /**
     * Adds a persistent effect that forbids executing ActionFamily.BUILD actions that target buildings
     * close to this player's pawns. Allow finishing a dome on a LEVEL3 building.
     */
    public static final Effect forbidBuildClose = (board, pawn, coordinate) -> {
        final Player owner = pawn.getOwner();
        final CheckEffect checkEffect = (b, p, c, a) -> {
            if (a.getFamily() == ActionFamily.BUILD) {
                return !c.anyNeighbouring(c2 -> b.getPawnAt(c2)
                            .map(p2 -> p2.getOwner().equals(owner))
                            .orElse(false)) ||
                        board.getBuildingAt(c).getLevel() == BuildingLevel.LEVEL3;
            }
            return true;
        };

        int duration = board.countPawns() / 2;
        board.addCheckEffect(duration, checkEffect);
        return false;
    };

    /**
     * Adds a persistent effect to the board that forbids executing ActionFamily.MOVE actions targeting the coordinate
     * where the pawn currently is for the current turn
     */
    public static final Effect forbidMoveBack = (board, pawn, coordinate) -> {
        final Coordinate playerPosition = pawn.getPosition();
        CheckEffect checkEffect = (b, p, c, a) -> !(c.equals(playerPosition) && a.getFamily() == ActionFamily.MOVE);
        board.addCheckEffect(1, checkEffect);
        return false;
    };

    /**
     * Adds a persistent effect to the board that forbids executing ActionFamily.BUILD actions targeting the target
     * coordinate for the current turn
     */
    public static final Effect forbidBuildAtCoordinate = (board, pawn, coordinate) -> {
        final Coordinate position = coordinate;
        CheckEffect checkEffect = (b, p, c, a) -> !c.equals(position) || a.getFamily() != ActionFamily.BUILD;
        board.addCheckEffect(1, checkEffect);
        return false;
    };

    /**
     * Adds a persistent effect to the board that forbids executing ActionFamily.BUILD actions targeting any
     * coordinate other than the current target for the current turn
     */
    public static final Effect forbidBuildAtOtherCoordinates = (board, pawn, coordinate) -> {
        final Coordinate position = coordinate;
        CheckEffect checkEffect = (b, p, c, a) -> c.equals(position) || a.getFamily() != ActionFamily.BUILD;
        board.addCheckEffect(1, checkEffect);
        return false;
    };
}
