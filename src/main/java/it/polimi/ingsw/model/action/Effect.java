package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.model.board.InvalidBuildException;
import it.polimi.ingsw.model.board.InvalidMoveException;
import it.polimi.ingsw.model.player.Pawn;

public interface Effect {
    boolean execute(Board board, Pawn pawn, Coordinate coordinate) throws InvalidMoveException, InvalidBuildException;
}
