package ingsw2020.group8.santorini.model.board;

import ingsw2020.group8.santorini.model.player.Pawn;

import java.util.Optional;

public class Cell {
    private Optional<Pawn> pawn;
    private Optional<Building> building;


    public Cell() {
        pawn = Optional.empty();
        building = Optional.empty();
    }
}
