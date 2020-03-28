package ingsw2020.group8.santorini.model.board;

import ingsw2020.group8.santorini.model.player.Pawn;
import ingsw2020.group8.santorini.model.player.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    Board board;
    Pawn pawn1;
    Pawn pawn2;

    @BeforeAll
    void prepareBoard() throws InvalidMoveException {
        board = new Board();
        pawn1 = new Pawn(new Player("test_1"), 1);
        pawn2 = new Pawn(new Player("test_2"), 1);

        testPutPawn();
    }

    @Test
    void testMovePawn() throws InvalidMoveException {
        Coordinate p0 = board.getPawnPosition(pawn1);
        Coordinate p1 = new Coordinate(3,0);
        board.movePawn(pawn1, p1);

        assertEquals(p1, board.getPawnPosition(pawn1));
        assertEquals(Optional.of(pawn1), board.getPawnAt(p1));
        assertEquals(Optional.empty(), board.getPawnAt(p0));
    }

    @Test
    void testSwapPawn() {
        Coordinate p_p1 = board.getPawnPosition(pawn1);
        Coordinate p_p2 = board.getPawnPosition(pawn2);
        board.swapPawn(pawn1, pawn2);

        assertEquals(p_p1, board.getPawnPosition(pawn2));
        assertEquals(p_p2, board.getPawnPosition(pawn1));

        assertEquals(Optional.of(pawn1), board.getPawnAt(p_p2));
        assertEquals(Optional.of(pawn2), board.getPawnAt(p_p1));
    }

    @Test
    void testBuildBlock() {
        Coordinate c = new Coordinate(1,2);
        assertEquals(Optional.empty(), board.getBuildingAt(c));

        board.buildBlock(c);

        assertTrue(board.getBuildingAt(c).isPresent());
        assertEquals(BuildingLevel.LEVEL1, board.getBuildingAt(c).get().getLevel());
    }

    @Test
    void testBuildDome() {
        Coordinate c = new Coordinate(2,2);
        assertEquals(Optional.empty(), board.getBuildingAt(c));

        board.buildBlock(c);

        assertFalse(board.getBuildingAt(c).get().hasDome());
        board.buildDome(c);

        assertTrue(board.getBuildingAt(c).get().hasDome());
    }

    @Test
    void testPutPawn() throws InvalidMoveException {
        Coordinate c1 = new Coordinate(1, 3);
        Coordinate c2 = new Coordinate(4,0);

        assertFalse(board.getPawnAt(c1).isPresent());
        assertFalse(board.getPawnAt(c2).isPresent());

        board.putPawn(pawn1, c1);
        assertTrue(board.getPawnAt(c1).isPresent());
        assertEquals(c1, board.getPawnPosition(pawn1));
        assertEquals(Optional.of(pawn1), board.getPawnAt(c1));

        board.putPawn(pawn2, c2);
        assertTrue(board.getPawnAt(c2).isPresent());
        assertEquals(c2, board.getPawnPosition(pawn2));
        assertEquals(Optional.of(pawn2), board.getPawnAt(c2));
    }

    @Test
    void testRemovePawn() throws InvalidMoveException {
        Pawn p = new Pawn(new Player("test_delete"), 1);
        Coordinate c = new Coordinate(4,4);

        board.putPawn(p, c);
        assertTrue(board.getPawnAt(c).isPresent());

        board.removePawn(p);
        assertFalse(board.getPawnAt(c).isPresent());
}
}