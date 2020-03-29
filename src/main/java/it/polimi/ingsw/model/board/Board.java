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
        return cells[c.getX()][c.getY()].pawn;
    }

    public Coordinate getPawnPosition(Pawn pawn) {
        return pawns.get(pawn);
    }

    public Optional<Building> getBuildingAt(Coordinate c) {
        return cells[c.getX()][c.getY()].building;
    }

    public void movePawn(Pawn pawn, Coordinate c) throws InvalidMoveException {
        if(!getPawnAt(c).isPresent()) {
            cells[pawns.get(pawn).getX()][pawns.get(pawn).getY()].pawn = Optional.empty();
            pawns.remove(pawn);
            pawns.put(pawn, c);
            cells[c.getX()][c.getY()].pawn = Optional.of(pawn);
        }
        else
            throw new InvalidMoveException();
    }

    public void swapPawn(Pawn pawn1, Pawn pawn2) {
        Coordinate t = pawns.get(pawn1);
        Optional<Pawn> tc = cells[pawns.get(pawn1).getX()][pawns.get(pawn1).getY()].pawn ;
        cells[pawns.get(pawn1).getX()][pawns.get(pawn1).getY()].pawn = cells[pawns.get(pawn2).getX()][pawns.get(pawn2).getY()].pawn;
        cells[pawns.get(pawn2).getX()][pawns.get(pawn2).getY()].pawn = tc;
        pawns.remove(pawn1);
        pawns.put(pawn1, pawns.get(pawn2));
        pawns.remove(pawn2);
        pawns.put(pawn2, t);
    }

    public void buildBlock(Coordinate c) throws InvalidBuildException{
        Optional<Building> ob = cells[c.getX()][c.getY()].building;
        if (ob.isPresent()) {
            ob.get().buildBlock();
        } else {
            cells[c.getX()][c.getY()].building = Optional.of(new Building());
        }
    }

    public void buildDome(Coordinate c)  throws InvalidBuildException{
        Optional<Building> ob = cells[c.getX()][c.getY()].building;
        if (ob.isPresent()) {
            ob.get().buildDome();
        } else {
            throw new InvalidBuildException();
        }
    }

    public void putPawn(Pawn pawn, Coordinate c) throws InvalidMoveException {
        if (!getPawnAt(c).isPresent()) {
            pawns.put(pawn, c);
            cells[c.getX()][c.getY()].pawn = Optional.of(pawn);
        } else
            throw new InvalidMoveException();
    }


    public void removePawn(Pawn pawn) {
        cells[pawns.get(pawn).getX()][pawns.get(pawn).getY()].pawn = Optional.empty();
        pawns.remove(pawn);
    }

    public Board() {
        cells = new Cell[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                cells[i][j] = new Cell();

    }
}

