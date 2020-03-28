package ingsw2020.group8.santorini.model.action;

import ingsw2020.group8.santorini.model.board.Board;
import ingsw2020.group8.santorini.model.board.Coordinate;
import ingsw2020.group8.santorini.model.player.Pawn;

public interface CheckAllowed {
    boolean isAllowed(Board board, Pawn pawn, Coordinate coordinate);
}