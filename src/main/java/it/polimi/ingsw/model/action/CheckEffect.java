package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.model.player.Pawn;

@FunctionalInterface
public interface CheckEffect {
    boolean isAllowed(Board board, Pawn pawn, Coordinate coordinate, Action action);
}
