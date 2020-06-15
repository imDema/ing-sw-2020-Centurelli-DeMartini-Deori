package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.model.player.Pawn;

/**
 * Atomic condition check intended to be composed with other checks via logical and.
 * Checks must not modify the state of the board
 */
@FunctionalInterface
public interface Check {
    /**
     * Evaluate the check for a pawn targeting a specific coordinate.
     * @param board Board on which the check should be performed
     * @param pawn Acting pawn
     * @param coordinate Target for the check
     * @return true if the check is passed, false otherwise
     */
    boolean isAllowed(Board board, Pawn pawn, Coordinate coordinate);
}
