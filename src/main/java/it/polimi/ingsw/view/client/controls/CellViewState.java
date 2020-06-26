package it.polimi.ingsw.view.client.controls;

import it.polimi.ingsw.model.board.Building;

import java.util.Optional;

/**
 * Client side representation of a cell on the board
 */
public class CellViewState {
    private boolean hasPawn = false;
    private PawnViewState pawn = null;
    private Building building = new Building();

    public Optional<PawnViewState> getPawn() {
        if (hasPawn) {
            return Optional.of(pawn);
        } else {
            return Optional.empty();
        }
    }

    public void putPawn(PawnViewState pawn) {
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

    public void setBuilding(Building building) {
        this.building = building;
    }
}
