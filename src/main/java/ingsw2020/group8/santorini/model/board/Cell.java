package ingsw2020.group8.santorini.model.board;

import ingsw2020.group8.santorini.model.player.Pawn;

import java.util.Optional;

public class Cell {
    public Optional<Pawn> pawn = Optional.empty();
    public Optional<Building> building = Optional.empty();
}
