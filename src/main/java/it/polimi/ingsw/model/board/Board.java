package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.player.Pawn;
import java.lang.UnsupportedOperationException;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Board {
    private final int BOARD_SIZE = 5;

    private Cell[][] cells;
    private Map<Pawn, Coordinate> pawns = new HashMap<>();

    public Optional<Pawn> getPawnAt(Coordinate c) {
        throw new UnsupportedOperationException();
    }

    public Coordinate getPawnPosition(Pawn pawn) {
        return pawns.get(pawn);
    }

    public Optional<Building> getBuildingAt(Coordinate c) {
        throw new UnsupportedOperationException();
    }

    public void movePawn(Pawn pawn, Coordinate c) throws InvalidMoveException {
        if(getPawnAt(c) == null) {
            pawns.remove(pawn);
            pawns.put(pawn, c);
        }
        else
            throw new InvalidMoveException();
    }

    public void swapPawn(Pawn pawn1, Pawn pawn2) {
        throw new UnsupportedOperationException();
    }

    public void buildBlock(Coordinate c) {
        throw new UnsupportedOperationException();
    }

    public void buildDome(Coordinate c) {
        throw new UnsupportedOperationException();
    }

    public void putPawn(Pawn pawn, Coordinate c) throws InvalidMoveException {
        if (getPawnAt(c) == null)
            pawns.put(pawn, c);
        else
            throw new InvalidMoveException();
    }

    public void removePawn(Pawn pawn) {
        pawns.remove(pawn);
    }

    public Board() {
        cells = new Cell[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                cells[i][j] = new Cell();

    }
}

