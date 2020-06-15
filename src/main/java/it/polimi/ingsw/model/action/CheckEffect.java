package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.model.player.Pawn;

/**
 * Intended to be used at a higher level than a {@link Check} to assess a condition with a higher priority
 * that can also impose restrictions on actions
 * @see Check
 * @see Action
 */
@FunctionalInterface
public interface CheckEffect {
    boolean isAllowed(Board board, Pawn pawn, Coordinate coordinate, Action action);
}
