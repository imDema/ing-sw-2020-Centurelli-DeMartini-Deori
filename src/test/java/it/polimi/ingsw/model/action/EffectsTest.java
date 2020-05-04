package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.board.Board;
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
        Pawn pawn1player1 = player1.getPawn(0);
        Pawn pawn1player2 = player2.getPawn(0);
        Pawn pawn2player1 = player1.getPawn(1);

        board.buildBlock(new Coordinate(1,1));
        board.buildBlock(new Coordinate(2,1));
        board.buildBlock(new Coordinate(2,1));
        board.buildBlock(new Coordinate(2,2));
        board.buildBlock(new Coordinate(2,2));
        board.buildBlock(new Coordinate(2,2));

        board.putPawn(pawn1player1,new Coordinate(2,1));
        board.putPawn(pawn1player2,new Coordinate(1,2));

        // TODO: suppose the checks true and verify just the effects
        // move
        assertTrue(Effects.move.execute(board,pawn1player1,new Coordinate(2,2)));
        assertFalse(Effects.move.execute(board,pawn1player2,new Coordinate(2,1)));

        // buildBlock
        assertFalse(Effects.buildBlock.execute(board,pawn1player2,new Coordinate(1,1)));

        board.buildBlock(new Coordinate(1,1));


        assertFalse(Effects.buildDome.execute(board,pawn1player1,new Coordinate(1,1)));



        // pushPawn
        board.putPawn(pawn2player1,new Coordinate(3,1));
        assertFalse(Effects.pushPawn.execute(board,pawn1player1,new Coordinate(2,1)));
        assertEquals(pawn1player2.getPosition(),new Coordinate(2,0));

        // swapPawns
        //Effects.swapPawns.execute(board,pawn2player1,pawn1player2.getPosition());
        //assertEquals(pawn2player1.getPosition(),new Coordinate(2,0));
        //assertEquals(pawn1player2.getPosition(),new Coordinate(3,1));
        //assertTrue(Effects.swapPawns.execute(board,pawn1player2,new Coordinate(2,2)));
        //assertFalse(Effects.swapPawns.execute(board,pawn1player2,new Coordinate(2,0)));

        // forbid current position


        // forbid coordinate

        // forbidOtherCoordinates

        //


    }

}