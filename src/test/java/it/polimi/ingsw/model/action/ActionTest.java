package it.polimi.ingsw.model.action;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.model.board.InvalidMoveException;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActionTest {

    // TODO:
    @Test
    void executeMove() {
        Board board = new Board();
        Player p1 = new Player("p1");
        Player p2 = new Player("p2");

        try {
            board.putPawn(p1.getPawn(0), new Coordinate(1,3));
            board.putPawn(p2.getPawn(0), new Coordinate(2,2));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // TODO:
            Action move = new Action(ActionKind.MOVE);
            move.execute(board, p1.getPawn(0), new Coordinate(2,2));
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }



    }

    // TODO:
    @Test
    void checkAllowed() {
    }
}