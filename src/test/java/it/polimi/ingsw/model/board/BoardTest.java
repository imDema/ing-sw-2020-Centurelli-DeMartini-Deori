package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    @Test
    public void testMovePawn() throws InvalidActionException {
        // Set up board for test
        Board board = new Board();
        Pawn[] pawns = setupTestPawns(board);

        Coordinate p0 = pawns[0].getPosition();
        Coordinate p1 = new Coordinate(0,3);
        board.movePawn(pawns[0], p1);

        assertEquals(p1, pawns[0].getPosition());
        assertEquals(Optional.of(pawns[0]), board.getPawnAt(p1));
        assertEquals(Optional.empty(), board.getPawnAt(p0));
    }

    @Test
    public void testSwapPawn() {
        // Set up board for test
        Board board = new Board();
        Pawn[] pawns = setupTestPawns(board);

        Coordinate p_p1 = pawns[0].getPosition();
        Coordinate p_p2 = pawns[1].getPosition();
        board.swapPawn(pawns[0], pawns[1]);

        assertEquals(p_p1, pawns[1].getPosition());
        assertEquals(p_p2, pawns[0].getPosition());

        assertEquals(Optional.of(pawns[0]), board.getPawnAt(p_p2));
        assertEquals(Optional.of(pawns[1]), board.getPawnAt(p_p1));
    }

    private Pawn[] setupTestPawns(Board board) {
        Pawn[] pawns = new Pawn[2];
        pawns[0] = new Pawn(new Player("test_1"), 0);
        pawns[1] = new Pawn(new Player("test_2"), 1);

        Coordinate c1 = new Coordinate(1, 3);
        Coordinate c2 = new Coordinate(4,0);
        try {
            board.putPawn(pawns[0], c1);
            board.putPawn(pawns[1], c2);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        return pawns;
    }

    @Test
    public void testBuildBlock() throws InvalidActionException {
        Board board = new Board();

        Coordinate c = new Coordinate(1,2);
        assertEquals(BuildingLevel.LEVEL0, board.getBuildingAt(c).getLevel());

        board.buildBlock(c);

        assertEquals(BuildingLevel.LEVEL1, board.getBuildingAt(c).getLevel());
    }

    @Test
    public void testBuildDome() throws InvalidActionException {
        Board board = new Board();

        Coordinate c = new Coordinate(2,2);

        assertFalse(board.getBuildingAt(c).hasDome());

        board.buildDome(c);

        assertTrue(board.getBuildingAt(c).hasDome());
    }

    @Test
    public void testPutPawn() throws InvalidActionException {
        Board board = new Board();

        Coordinate c1 = new Coordinate(0, 2);
        Pawn pawn = new Pawn(new Player ("test_put"), 2);

        assertFalse(board.getPawnAt(c1).isPresent());

        board.putPawn(pawn, c1);
        assertTrue(board.getPawnAt(c1).isPresent());
        assertEquals(c1, pawn.getPosition());
        assertEquals(Optional.of(pawn), board.getPawnAt(c1));
    }

    @Test
    public void testRemovePawn() throws InvalidActionException {
        Board board = new Board();

        Pawn p = new Pawn(new Player("test_delete"), 1);
        Coordinate c = new Coordinate(4,4);

        board.putPawn(p, c);
        assertTrue(board.getPawnAt(c).isPresent());

        board.removePawn(p);
        assertFalse(board.getPawnAt(c).isPresent());
    }
}
