package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.BuildingLevel;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.model.board.InvalidActionException;
import it.polimi.ingsw.model.player.God;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntegrationTest {
    @Test
    public void testBasicGameSequence() throws InvalidActionException {
        // Init lobby
        Lobby lobby = new Lobby();
        lobby.setSize(3);
        lobby.loadGods();

        // Init players
        User u1 = new User("user_1");
        User u2 = new User("user_2");
        User u3 = new User("user_3");

        lobby.addUser(u1);
        lobby.addUser(u2);
        lobby.addUser(u3);

        List<God> gods = lobby.getAvailableGods();
        assertTrue(gods.size() > 0);
        lobby.chooseGod(u1, gods.get(0));

        gods = lobby.getAvailableGods();
        lobby.chooseGod(u2, gods.get(1));

        gods = lobby.getAvailableGods();
        lobby.chooseGod(u3, gods.get(2));


        // Place pawns at starting positions
        assertTrue(lobby.getUserToSetUp().isPresent());
        lobby.setUpUserPawns(lobby.getUserToSetUp().get(), new Coordinate(0,0), new Coordinate(1,0));

        assertTrue(lobby.getUserToSetUp().isPresent());
        lobby.setUpUserPawns(lobby.getUserToSetUp().get(), new Coordinate(2,0), new Coordinate(3,0));

        assertTrue(lobby.getUserToSetUp().isPresent());
        lobby.setUpUserPawns(lobby.getUserToSetUp().get(), new Coordinate(4,0), new Coordinate(0,1));

        assertTrue(lobby.isGameReady());

        Game game = lobby.getGame();
        Board board = game.getBoard();

        // Start game cycle
        Player player = game.getCurrentPlayer();
        Action[] actions = player.nextStep(Action.start);
        Pawn pawn = player.getPawn(0);
        Action chosenAction = actions[0];
        Coordinate coordinate = new Coordinate(1,1);

        // Check and execute action
        assertTrue(board.checkAction(chosenAction, pawn, coordinate));
        board.executeAction(chosenAction, pawn, coordinate);
        assertEquals(game.getPlayers().get(0).getPawn(0).getPosition(), coordinate);

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
