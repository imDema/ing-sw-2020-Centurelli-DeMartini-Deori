package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    Board board = new Board();
    Pawn pawn1;
    Pawn pawn2;

    @Test
    public void testMovePawn() throws InvalidMoveException {
        setupTestPawns();
        Coordinate p0 = board.getPawnPosition(pawn1);
        Coordinate p1 = new Coordinate(0,3);
        board.movePawn(pawn1, p1);

        assertEquals(p1, board.getPawnPosition(pawn1));
        assertEquals(Optional.of(pawn1), board.getPawnAt(p1));
        assertEquals(Optional.empty(), board.getPawnAt(p0));
    }

    @Test
    public void testSwapPawn() {
        setupTestPawns();
        Coordinate p_p1 = board.getPawnPosition(pawn1);
        Coordinate p_p2 = board.getPawnPosition(pawn2);
        board.swapPawn(pawn1, pawn2);

        assertEquals(p_p1, board.getPawnPosition(pawn2));
        assertEquals(p_p2, board.getPawnPosition(pawn1));

        assertEquals(Optional.of(pawn1), board.getPawnAt(p_p2));
        assertEquals(Optional.of(pawn2), board.getPawnAt(p_p1));
    }

    private void setupTestPawns() {
        pawn1 = new Pawn(new Player("test_1"), 1);
        pawn2 = new Pawn(new Player("test_2"), 1);

        Coordinate c1 = new Coordinate(1, 3);
        Coordinate c2 = new Coordinate(4,0);
        try {
            board.putPawn(pawn1, c1);
            board.putPawn(pawn2, c2);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testBuildBlock() {
        Coordinate c = new Coordinate(1,2);
        assertEquals(Optional.empty(), board.getBuildingAt(c));

        board.buildBlock(c);

        assertTrue(board.getBuildingAt(c).isPresent());
        assertEquals(BuildingLevel.LEVEL1, board.getBuildingAt(c).get().getLevel());
    }

    @Test
    public void testBuildDome() {
        Coordinate c = new Coordinate(2,2);
        assertEquals(Optional.empty(), board.getBuildingAt(c));

        board.buildBlock(c);

        assertFalse(board.getBuildingAt(c).get().hasDome());
        board.buildDome(c);

        assertTrue(board.getBuildingAt(c).get().hasDome());
    }

    @Test
    public void testPutPawn() throws InvalidMoveException {
        Coordinate c1 = new Coordinate(0, 2);
        pawn1 = new Pawn(new Player ("test_put"), 2);

        assertFalse(board.getPawnAt(c1).isPresent());

        board.putPawn(pawn1, c1);
        assertTrue(board.getPawnAt(c1).isPresent());
        assertEquals(c1, board.getPawnPosition(pawn1));
        assertEquals(Optional.of(pawn1), board.getPawnAt(c1));
    }

    @Test
    public void testRemovePawn() throws InvalidMoveException {
        Pawn p = new Pawn(new Player("test_delete"), 1);
        Coordinate c = new Coordinate(4,4);

        board.putPawn(p, c);
        assertTrue(board.getPawnAt(c).isPresent());

        board.removePawn(p);
        assertFalse(board.getPawnAt(c).isPresent());
    }
}