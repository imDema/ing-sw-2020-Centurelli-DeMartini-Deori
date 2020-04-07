package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.action.CheckAllowed;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Board {
    public final int BOARD_SIZE = 5;

    private Cell[][] cells;
    private Map<Player, CheckAllowed> activeCheckEffects = new HashMap<>(); //TEMP


    public Optional<Pawn> getPawnAt(Coordinate c) {
        return cellAt(c).getPawn();
    }

    public Building getBuildingAt(Coordinate c) {
        return cellAt(c).getBuilding();
    }

    public void movePawn(Pawn pawn, Coordinate c) throws InvalidActionException {
        if(!getPawnAt(c).isPresent()) {
            Coordinate oldC = pawn.getPosition();

            cellAt(oldC).removePawn();
            cellAt(c).putPawn(pawn);

            pawn.setPosition(c);
        }
        else
            throw new InvalidActionException();
    }

    public void swapPawn(Pawn pawn1, Pawn pawn2) {
        Coordinate c1 = pawn1.getPosition();
        Coordinate c2 = pawn2.getPosition();

        cellAt(c1).putPawn(pawn2);
        cellAt(c2).putPawn(pawn1);

        pawn1.setPosition(c2);
        pawn2.setPosition(c1);
    }

    public void buildBlock(Coordinate c) throws InvalidActionException{
         cellAt(c)
             .getBuilding()
             .buildBlock();
    }

    public void buildDome(Coordinate c)  throws InvalidActionException{
        cellAt(c)
            .getBuilding()
            .buildDome();
    }

    public void putPawn(Pawn pawn, Coordinate c) throws InvalidActionException {
        if (!getPawnAt(c).isPresent()) {
            cellAt(c).putPawn(pawn);
            pawn.setPosition(c);
        } else
            throw new InvalidActionException();
    }

    public boolean checkAction(Action action, Pawn pawn, Coordinate c) {
        boolean flag = true;
        for (CheckAllowed lambda : activeCheckEffects.values()) {
            if (!lambda.isAllowed(this, pawn, c))
            {
                flag = false;
                break;
            }
        }
        return flag && action.checkAllowed(this,pawn,c);
    }

    public void executeAction(Action action, Pawn pawn, Coordinate c) throws InvalidActionException {
        action.execute(this, pawn, c);
    }

    public void resetCheckEffect(Player player) {
        activeCheckEffects.remove(player);
    }

    public void setCheckEffect(Player player, CheckAllowed checkAllowed) {
        activeCheckEffects.put(player, checkAllowed);
    }


    public void removePawn(Pawn pawn) {
        Coordinate c = pawn.getPosition();
        cellAt(c).removePawn();
    }

    private Cell cellAt(Coordinate c) {
        return cells[c.getX()][c.getY()];
    }

    public Board() {
        cells = new Cell[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                cells[i][j] = new Cell();

    }
}

