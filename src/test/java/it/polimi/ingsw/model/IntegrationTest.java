package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.BuildingLevel;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.model.board.InvalidActionException;
import it.polimi.ingsw.model.player.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTest {
    @Test
    public void testBasicGameSequence() throws InvalidActionException {
        // Init lobby
        Lobby lobby = new Lobby(3);
        God[] gods;
        try {
            gods = lobby.getAvailableGods();
        } catch (IOException ex) {
            ex.printStackTrace();
            fail();
            return;
        }

        // Init players
        Player p1 = new Player(new User("user_1"), gods[0]);
        Player p2 = new Player(new User("user_2"), gods[1]);
        Player p3 = new Player(new User("user_3"), gods[2]);

        // Add players to lobby
        lobby.addPlayer(p1);
        lobby.addPlayer(p2);
        lobby.addPlayer(p3);

        // Instantiate board
        Game game = lobby.createGame();
        Board board = game.getBoard();

        // Place pawns at starting positions
        board.putPawn(p1.getPawn(0), new Coordinate(0,0));
        board.putPawn(p1.getPawn(1), new Coordinate(1,0));

        board.putPawn(p2.getPawn(0), new Coordinate(2,0));
        board.putPawn(p2.getPawn(1), new Coordinate(3,0));

        board.putPawn(p3.getPawn(0), new Coordinate(4,0));
        board.putPawn(p3.getPawn(1), new Coordinate(0,1));

        // Start game cycle
        Player player = game.getCurrentPlayer();
        Action[] actions = player.nextStep(Action.start);
        Pawn pawn = player.getPawn(0);
        Action chosenAction = actions[0];
        Coordinate coordinate = new Coordinate(1,1);

        // Check and execute action
        assertTrue(board.checkAction(chosenAction, pawn, coordinate));
        board.executeAction(chosenAction, pawn, coordinate);
        assertEquals(p1.getPawn(0).getPosition(), coordinate);

        // Advance turn sequence and go on executing actions
        actions = player.nextStep(chosenAction);
        chosenAction = actions[0];
        coordinate = new Coordinate(2,1);

        assertTrue(board.checkAction(chosenAction, pawn, coordinate));
        board.executeAction(chosenAction, pawn, coordinate);
        assertEquals(BuildingLevel.LEVEL1, board.getBuildingAt(coordinate).getLevel());

        // Expect end turn
        actions = player.nextStep(chosenAction);
        assertEquals(1, actions.length);
        assertEquals(Action.endTurn, actions[0]);

        // End turn signals to advance the turn
        game.nextTurn();

        // Back to turn cycle
        player = game.getCurrentPlayer();
        actions = player.nextStep(Action.start);
        chosenAction = actions[0];
        pawn = player.getPawn(1);
        coordinate = new Coordinate(4,1);

        assertTrue(board.checkAction(chosenAction,pawn,coordinate));
        board.executeAction(chosenAction, pawn, coordinate);
        assertEquals(coordinate, player.getPawn(1).getPosition());
    }
}
