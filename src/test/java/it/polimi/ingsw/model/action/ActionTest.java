package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.model.board.InvalidActionException;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ActionTest {

    // TODO:
    @Test
    private void testExecuteMove() throws InvalidActionException {
        Board board = new Board();
        Player p1 = new Player("p1");
        Player p2 = new Player("p2");

        board.putPawn(p1.getPawn(0), new Coordinate(1,3));
        board.putPawn(p2.getPawn(0), new Coordinate(2,2));

        throw new UnsupportedOperationException();
    }

    // TODO: change to public and write tests
    @Test
    private void testCheckAllowed() {
        throw new UnsupportedOperationException();
    }
}