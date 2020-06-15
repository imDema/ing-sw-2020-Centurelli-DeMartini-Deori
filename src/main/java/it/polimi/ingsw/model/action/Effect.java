package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.model.board.InvalidActionException;
import it.polimi.ingsw.model.player.Pawn;

/**
 * Atomic board modifying effect
 */
@FunctionalInterface
public interface Effect {
    /**
     * Apply the effect on the board
     * @param board Board on which the effect should be performed
     * @param pawn Acting pawn
     * @param coordinate Target coordinate for the effect
     * @return true if the execution of this effect causes the player to win the game
     * @throws InvalidActionException if the effect would violate the game rules with the selected board, pawn,
     * coordinate combination
     */
    boolean execute(Board board, Pawn pawn, Coordinate coordinate) throws InvalidActionException;
}
