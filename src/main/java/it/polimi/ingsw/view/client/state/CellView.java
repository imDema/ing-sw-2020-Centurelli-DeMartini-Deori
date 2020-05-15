package it.polimi.ingsw.view.client.state;

import it.polimi.ingsw.model.board.Building;

import java.util.Optional;

public class CellView {
    private boolean hasPawn = false;
    private PawnView pawn = null;
    private Building building = new Building();

    public Optional<PawnView> getPawn() {
        if (hasPawn) {
            return Optional.of(pawn);
        } else {
            return Optional.empty();
        }
    }

    public void putPawn(PawnView pawn) {
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
