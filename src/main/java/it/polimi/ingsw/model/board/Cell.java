package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.player.Pawn;

import java.util.Optional;

public class Cell {
    public Optional<Pawn> pawn = Optional.empty();
    public Optional<Building> building = Optional.empty();
}
