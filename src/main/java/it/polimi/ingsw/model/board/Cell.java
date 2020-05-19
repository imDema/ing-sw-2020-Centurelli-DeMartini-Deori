package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.player.Pawn;

import java.util.Optional;

/**
 * The Cell class it's used in the Board, every cell of the board is represented by an istance of the
 * cell class;
 * a cell has 3 fields: hasPawn (a boolean that is set to true when the cell has a dome on top of the building)
 * pawn (the pawn that is on the cell, it can be null if the cell is empty)
 * building (the building on the cell, it can be null if any player has built on it yet)
 */
public class Cell {
    private boolean hasPawn = false;
    private Pawn pawn = null;

    private Building building = new Building();

    public Optional<Pawn> getPawn() {
        if (hasPawn) {
            return Optional.of(pawn);
        } else {
            return Optional.empty();
        }
    }

    public void putPawn(Pawn pawn) {
        this.pawn = pawn;
        this.hasPawn = true;
    }

    public void removePawn() {
        this.pawn = null;
        this.hasPawn = false;
    }

    public Building getBuilding() {
        return building;
    }
}
