package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    @Test
    public void testMovePawn() throws InvalidMoveException {
        // Set up board for test
        Board board = new Board();
        Pawn[] pawns = setupTestPawns(board);

        Coordinate p0 = board.getPawnPosition(pawns[0]);
        Coordinate p1 = new Coordinate(0,3);
        board.movePawn(pawns[0], p1);

        assertEquals(p1, board.getPawnPosition(pawns[0]));
        assertEquals(Optional.of(pawns[0]), board.getPawnAt(p1));
        assertEquals(Optional.empty(), board.getPawnAt(p0));
    }

    @Test
    public void testSwapPawn() {
        // Set up board for test
        Board board = new Board();
        Pawn[] pawns = setupTestPawns(board);

        Coordinate p_p1 = board.getPawnPosition(pawns[0]);
        Coordinate p_p2 = board.getPawnPosition(pawns[1]);
        board.swapPawn(pawns[0], pawns[1]);

        assertEquals(p_p1, board.getPawnPosition(pawns[1]));
        assertEquals(p_p2, board.getPawnPosition(pawns[0]));

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
    public void testBuildBlock() throws InvalidBuildException{
        Board board = new Board();

        Coordinate c = new Coordinate(1,2);
        assertEquals(Optional.empty(), board.getBuildingAt(c));

        board.buildBlock(c);

        assertTrue(board.getBuildingAt(c).isPresent());
        assertEquals(BuildingLevel.LEVEL1, board.getBuildingAt(c).get().getLevel());
    }

    @Test
    public void testBuildDome() throws InvalidBuildException{
        Board board = new Board();

        Coordinate c = new Coordinate(2,2);
        assertEquals(Optional.empty(), board.getBuildingAt(c));

        board.buildBlock(c);

        assertFalse(board.getBuildingAt(c).get().hasDome());
        board.buildDome(c);

        assertTrue(board.getBuildingAt(c).get().hasDome());
    }

    @Test
    public void testPutPawn() throws InvalidMoveException {
        Board board = new Board();

        Coordinate c1 = new Coordinate(0, 2);
        Pawn pawn = new Pawn(new Player ("test_put"), 2);

        assertFalse(board.getPawnAt(c1).isPresent());

        board.putPawn(pawn, c1);
        assertTrue(board.getPawnAt(c1).isPresent());
        assertEquals(c1, board.getPawnPosition(pawn));
        assertEquals(Optional.of(pawn), board.getPawnAt(c1));
    }

    @Test
    public void testRemovePawn() throws InvalidMoveException {
        Board board = new Board();

        Pawn p = new Pawn(new Player("test_delete"), 1);
        Coordinate c = new Coordinate(4,4);

        board.putPawn(p, c);
        assertTrue(board.getPawnAt(c).isPresent());

        board.removePawn(p);
        assertFalse(board.getPawnAt(c).isPresent());
    }
}
