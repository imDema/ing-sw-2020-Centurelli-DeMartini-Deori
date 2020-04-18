package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.player.Pawn;

import java.util.Optional;

public abstract class Effects {
    public static final Effect move = (b, p, c) -> {
        BuildingLevel oldLevel = b.getBuildingAt(p.getPosition()).getLevel();
        BuildingLevel newLevel = b.getBuildingAt(c).getLevel();

        b.movePawn(p,c);
        //Check win condition
        return oldLevel != BuildingLevel.LEVEL3 && newLevel == BuildingLevel.LEVEL3;
    };

    public static final Effect buildBlock = (b, p, c) -> {
        b.buildBlock(c);
        return false;
    };

    public static final Effect buildDome = (b, p, c) -> {
        b.buildDome(c);
        return false;
    };

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

    public static final Effect swapPawns = (b, p, c) -> {
        BuildingLevel oldLevel = b.getBuildingAt(p.getPosition()).getLevel();
        BuildingLevel newLevel = b.getBuildingAt(c).getLevel();

        b.getPawnAt(c)
                .ifPresent(p2 -> b.swapPawn(p, p2));

        b.movePawn(p,c);

        //Check win condition
        return oldLevel != BuildingLevel.LEVEL3 && newLevel == BuildingLevel.LEVEL3;
    };
}
