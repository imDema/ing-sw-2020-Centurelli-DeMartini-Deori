package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.player.Pawn;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Board {
    private final int BOARD_SIZE = 5;

    private Cell[][] cells;
    private Map<Pawn, Coordinate> pawns = new HashMap<>();

    public Optional<Pawn> getPawnAt(Coordinate c) {
        return cellAt(c).getPawn();
    }

    public Coordinate getPawnPosition(Pawn pawn) {
        return pawns.get(pawn);
    }

    public Building getBuildingAt(Coordinate c) {
        return cellAt(c).getBuilding();
    }

    public void movePawn(Pawn pawn, Coordinate c) throws InvalidMoveException {
        if(!getPawnAt(c).isPresent()) {
            Coordinate oldC = getPawnPosition(pawn);

            cellAt(oldC).removePawn();
            cellAt(c).putPawn(pawn);

            pawns.replace(pawn, c);
        }
        else
            throw new InvalidMoveException();
    }

    public void swapPawn(Pawn pawn1, Pawn pawn2) {
        Coordinate c1 = getPawnPosition(pawn1);
        Coordinate c2 = getPawnPosition(pawn2);

        cellAt(c1).putPawn(pawn2);
        cellAt(c2).putPawn(pawn1);

        pawns.replace(pawn1, c2);
        pawns.replace(pawn2, c1);
    }

    public void buildBlock(Coordinate c) throws InvalidBuildException{
         cellAt(c)
                 .getBuilding()
                 .buildBlock();
    }

    public void buildDome(Coordinate c)  throws InvalidBuildException{
        cellAt(c)
                .getBuilding()
                .buildDome();
    }

    public void putPawn(Pawn pawn, Coordinate c) throws InvalidMoveException {
        if (!getPawnAt(c).isPresent()) {
            cellAt(c).putPawn(pawn);
            pawns.put(pawn, c);
        } else
            throw new InvalidMoveException();
    }


    public void removePawn(Pawn pawn) {
        Coordinate c = getPawnPosition(pawn);
        cellAt(c).removePawn();
        pawns.remove(pawn);
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

