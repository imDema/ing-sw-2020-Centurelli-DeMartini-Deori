package it.polimi.ingsw.view.client.state;

import it.polimi.ingsw.model.board.Building;

import java.util.Optional;

public class CellViewModel {
    private boolean hasPawn = false;
    private PawnViewModel pawn = null;
    private Building building = new Building();

    public Optional<PawnViewModel> getPawn() {
        if (hasPawn) {
            return Optional.of(pawn);
        } else {
            return Optional.empty();
        }
    }

    public void putPawn(PawnViewModel pawn) {
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
