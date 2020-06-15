package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.player.Pawn;

import java.util.Optional;

/**
 * Single cell on the {@link Board} grid. Contains a {@link Building} and can contain a {@link Pawn}
 */
public class Cell {
    private boolean hasPawn = false;
    private Pawn pawn = null;

    private final Building building = new Building();

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
