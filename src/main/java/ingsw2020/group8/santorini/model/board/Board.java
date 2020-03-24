package ingsw2020.group8.santorini.model.board;

import ingsw2020.group8.santorini.model.player.Pawn;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Optional;

public class Board {
    public Optional<Pawn> getPawnAt(Coordinate c) {throw new NotImplementedException();}

    public Optional<Building> getBuildingAt(Coordinate c) {throw new NotImplementedException();}

    public void movePawn(Coordinate c1, Coordinate c2){throw new NotImplementedException();}

    public void swapPawn(Coordinate c1, Coordinate c2) {throw new NotImplementedException();}

    public void buildBlock(Coordinate c) {throw new NotImplementedException();}

    public void buildDome(Coordinate c) {throw new NotImplementedException();}
}
