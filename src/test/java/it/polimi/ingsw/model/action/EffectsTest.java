package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.BuildingLevel;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.model.board.InvalidActionException;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EffectsTest {

    @Test
    public void testEffects() throws InvalidActionException {
        Board board = new Board();
        Player player1 = new Player("p1",null);
        Player player2 = new Player("p2",null);
        Pawn p1pw1 = player1.getPawn(0);
        Pawn p1pw2 = player1.getPawn(1);
        Pawn p2pw1 = player2.getPawn(0);
        Pawn p2pw2 = player2.getPawn(1);

        board.buildBlock(new Coordinate(1,1));

        board.buildBlock(new Coordinate(2,1));
        board.buildBlock(new Coordinate(2,1));

        board.buildBlock(new Coordinate(1,3));
        board.buildBlock(new Coordinate(1,3));
        board.buildBlock(new Coordinate(1,3));

        board.buildBlock(new Coordinate(2,2));
        board.buildBlock(new Coordinate(2,2));
        board.buildBlock(new Coordinate(2,2));

        // move
        board.putPawn(p1pw1, new Coordinate(1, 1));
        assertFalse(Effects.move.execute(board, p1pw1, new Coordinate(1, 2)));
        assertTrue(board.getPawnAt(new Coordinate(1, 1)).isEmpty());
        assertTrue(board.getPawnAt(new Coordinate(1, 2)).isPresent());
        assertEquals(board.getPawnAt(new Coordinate(1, 2)).get(), p1pw1);
        assertTrue(Effects.move.execute(board, p1pw1, new Coordinate(1, 3)));

        // winOnJumpDown
        board.removePawn(p1pw1);
        board.putPawn(p1pw1, new Coordinate(2, 1));
        assertFalse(Effects.winOnJumpDown.execute(board, p1pw1, new Coordinate(1,1)));
        assertTrue(Effects.winOnJumpDown.execute(board, p1pw1, new Coordinate(0,1)));

        // buildBlock
        assertEquals(board.getBuildingAt(new Coordinate(0, 2)).getLevel(), BuildingLevel.LEVEL0);
        assertFalse(Effects.buildBlock.execute(board, p1pw1, new Coordinate(0, 2)));
        assertEquals(board.getBuildingAt(new Coordinate(0, 2)).getLevel(), BuildingLevel.LEVEL1);

        // buildDome
        assertFalse(board.getBuildingAt(new Coordinate(0, 1)).hasDome());
        assertFalse(Effects.buildDome.execute(board, p1pw1, new Coordinate(0, 1)));
        assertTrue(board.getBuildingAt(new Coordinate(0, 1)).hasDome());

        // pushPawn
        board.removePawn(p1pw1);
        board.putPawn(p1pw1, new Coordinate(1, 2));
        board.putPawn(p2pw1, new Coordinate(1, 3));
        assertFalse(Effects.pushPawn.execute(board, p1pw1, p2pw1.getPosition()));
        assertTrue(board.getPawnAt(new Coordinate(1, 4)).isPresent());
        assertTrue(board.getPawnAt(new Coordinate(1, 2)).isPresent());
        assertTrue(board.getPawnAt(new Coordinate(1, 3)).isEmpty());
        assertEquals(board.getPawnAt(new Coordinate(1, 2)).orElseThrow(), p1pw1);
        assertEquals(board.getPawnAt(new Coordinate(1, 4)).orElseThrow(), p2pw1);

        // swapPawn
        board.removePawn(p1pw1);
        board.removePawn(p2pw1);
        board.putPawn(p1pw1, new Coordinate(1, 2));
        board.putPawn(p2pw1, new Coordinate(1, 3));
        assertTrue(Effects.swapPawns.execute(board, p1pw1, new Coordinate(1, 3)));
        assertEquals(p2pw1.getPosition(), new Coordinate(1, 2));
        assertEquals(p1pw1.getPosition(), new Coordinate(1, 3));

        // forbidMoveUp
        board.removePawn(p2pw1);
        board.putPawn(p1pw1, new Coordinate(1, 2));


    }

}