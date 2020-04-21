package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.model.board.InvalidActionException;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ActionTest {

    @Test
    public void testCheckAllowed() throws InvalidActionException {
        Board board = new Board();
        Player player1 = new Player("p1", null);
        Player player2 = new Player("p2", null);
        Pawn pawn1player1 = player1.getPawn(0);
        Pawn pawn1player2 = player2.getPawn(0);
        board.putPawn(pawn1player1, new Coordinate(2,1));
        board.putPawn(pawn1player2, new Coordinate(1,2));

        // [1][1] = LEVEL1
        // [2][2] = LEVEL2
        // [1][2] = LEVEL3
        // [2][0] = DOME
        board.buildBlock(new Coordinate(1,1));
        board.buildBlock(new Coordinate(2,2));
        board.buildBlock(new Coordinate(2,2));
        board.buildBlock(new Coordinate(1,2));
        board.buildBlock(new Coordinate(1,2));
        board.buildBlock(new Coordinate(1,2));
        board.buildBlock(new Coordinate(2,0));
        board.buildBlock(new Coordinate(2,0));
        board.buildBlock(new Coordinate(2,0));
        board.buildDome(new Coordinate(2,0));

        //
        // MOVE
        final Action testAction1 = new Action("Move", ActionFamily.MOVE,
                new Effect[] {Effects.move},
                new Check[] {});
        assertTrue(testAction1.checkAllowed(board,pawn1player1, new Coordinate(1,1)));
        assertTrue(testAction1.checkAllowed(board,pawn1player1, new Coordinate(2,2)));
        assertTrue(testAction1.checkAllowed(board,pawn1player1, new Coordinate(1,2)));
        assertTrue(testAction1.checkAllowed(board,pawn1player1, new Coordinate(4,4)));
        assertTrue(testAction1.checkAllowed(board,pawn1player1, new Coordinate(2,0)));


        final Action testAction2 = new Action("Move", ActionFamily.MOVE,
                new Effect[] {Effects.move},
                new Check[] {
                        Checks.neighbour
        });
        assertTrue(testAction2.checkAllowed(board,pawn1player1, new Coordinate(1,1)));
        assertTrue(testAction2.checkAllowed(board,pawn1player1, new Coordinate(2,2)));
        assertTrue(testAction2.checkAllowed(board,pawn1player1, new Coordinate(1,2)));
        assertFalse(testAction2.checkAllowed(board,pawn1player1, new Coordinate(4,4)));
        assertTrue(testAction2.checkAllowed(board,pawn1player1, new Coordinate(2,0)));


        final Action testAction3 = new Action("Move", ActionFamily.MOVE,
                new Effect[] {Effects.move},
                new Check[] {
                        Checks.notOccupied,
                        Checks.neighbour
                });
        assertTrue(testAction3.checkAllowed(board,pawn1player1, new Coordinate(1,1)));
        assertTrue(testAction3.checkAllowed(board,pawn1player1, new Coordinate(2,2)));
        assertFalse(testAction3.checkAllowed(board,pawn1player1, new Coordinate(1,2)));
        assertFalse(testAction3.checkAllowed(board,pawn1player1, new Coordinate(4,4)));
        assertTrue(testAction3.checkAllowed(board,pawn1player1, new Coordinate(2,0)));
    }

    @Test
    public void testWinCondition() throws InvalidActionException {
        Board board = new Board();
        Player player1 = new Player("player1", null);
        Pawn pawn1player1 = player1.getPawn(0);
        Pawn pawn2player1 = player1.getPawn(1);
        Player player2 = new Player("player2", null);
        Pawn pawn1player2 = player2.getPawn(0);
        board.putPawn(pawn1player1, new Coordinate(2,1));
        board.putPawn(pawn2player1, new Coordinate(4,3));
        board.putPawn(pawn1player2, new Coordinate(2,2));

        // [1][1] = LEVEL3
        // [2][1] = LEVEL2
        // [2][2] = LEVEL3
        // [3][3] = LEVEL1
        board.buildBlock(new Coordinate(1,1));
        board.buildBlock(new Coordinate(1,1));
        board.buildBlock(new Coordinate(1,1));
        board.buildBlock(new Coordinate(2,1));
        board.buildBlock(new Coordinate(2,1));
        board.buildBlock(new Coordinate(2,2));
        board.buildBlock(new Coordinate(2,2));
        board.buildBlock(new Coordinate(2,2));
        board.buildBlock(new Coordinate(3,3));
        board.buildBlock(new Coordinate(1,3));
        board.buildBlock(new Coordinate(1,3));
        board.buildBlock(new Coordinate(1,3));


        final Action move = new Action("Move", ActionFamily.MOVE,
                new Effect[] {Effects.move},
                new Check[] {
                        Checks.neighbour,
                        Checks.maxOneLevelAbove,
                        Checks.noDome,
                        Checks.notOccupied});

        assertTrue(move.execute(board,pawn1player1, new Coordinate(1,1)));
        assertFalse(move.execute(board,pawn1player2, new Coordinate(1,3)));
        assertFalse(move.execute(board,pawn2player1, new Coordinate(3,3)));
        assertFalse(move.execute(board, pawn1player1, new Coordinate(1,0)));
        assertFalse(move.execute(board,pawn1player1, new Coordinate(0,0)));
    }
}