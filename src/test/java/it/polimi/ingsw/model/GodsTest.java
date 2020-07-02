package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.model.board.InvalidActionException;
import it.polimi.ingsw.model.player.God;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GodsTest {

    public Lobby initLobby(String god1, String god2, String god3){
        Lobby lobby = new Lobby();
        lobby.setSize(3);
        User user1 = new User("user_1");
        User user2 = new User("user_2");
        User user3 = new User("user_3");
        lobby.addUser(user1);
        lobby.addUser(user2);
        lobby.addUser(user3);
        List<God> gods = lobby.getAllGods();
        List<God> godsAvailable = new ArrayList<>();
        godsAvailable.add(gods.stream().filter(g -> g.getName().equals(god1)).findFirst().orElseThrow());
        godsAvailable.add(gods.stream().filter(g -> g.getName().equals(god2)).findFirst().orElseThrow());
        godsAvailable.add(gods.stream().filter(g -> g.getName().equals(god3)).findFirst().orElseThrow());

        lobby.setAvailableGods(godsAvailable);
        gods = lobby.getAvailableGods();
        lobby.chooseGod(user2, gods.get(0));

        gods = lobby.getAvailableGods();
        lobby.chooseGod(user3, gods.get(0));

        gods = lobby.getAvailableGods();
        lobby.chooseGod(user1, gods.get(0));
        return lobby;
    }

    public void checkExec(Board board, Action chosenAction, Pawn pawn, Coordinate coordinate, Boolean b) throws InvalidActionException {
        if(b) {
            assertTrue(board.checkAction(chosenAction, pawn, coordinate));
            chosenAction.execute(board, pawn, coordinate);
        }
        else {
            assertFalse(board.checkAction(chosenAction, pawn, coordinate));
        }
    }

    @Test
    public void testAtlasDemeterPan() throws InvalidActionException {
        Lobby lobby = initLobby("Pan", "Atlas", "Demeter");

        // Place pawns at starting positions
        lobby.setUpUserPawns(lobby.getUserToSetUp().orElseThrow(), new Coordinate(2, 4), new Coordinate(2, 3));
        lobby.setUpUserPawns(lobby.getUserToSetUp().orElseThrow(), new Coordinate(0, 3), new Coordinate(0, 2));
        lobby.setUpUserPawns(lobby.getUserToSetUp().orElseThrow(), new Coordinate(1, 1), new Coordinate(4, 0));

        assertTrue(lobby.isGameReady());

        Game game = lobby.getGame();
        Board board = game.getBoard();

        // Setup
        board.buildBlock(new Coordinate(1,4));
        board.buildBlock(new Coordinate(1,3));
        board.buildBlock(new Coordinate(1,3));
        board.buildBlock(new Coordinate(2,4));
        board.buildBlock(new Coordinate(2,3));
        board.buildBlock(new Coordinate(2,3));

        // Turn 1 Pan
        Player player = game.getCurrentPlayer();
        Action[] actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(3, 4), true );
        assertFalse(actions[0].execute(board, player.getPawn(0), new Coordinate(3, 4)));

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(4, 4), true );
        game.nextTurn();

        // Turn 1 Atlas
        player = game.getCurrentPlayer();
        actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(0, 4), true );

        actions = player.nextStep(actions[0]);
        assertTrue(board.checkAction(actions[1], player.getPawn(0), new Coordinate(0, 3)));
        assertTrue(board.checkAction(actions[1], player.getPawn(0), new Coordinate(1, 4)));
        checkExec(board, actions[1], player.getPawn(0), new Coordinate(1, 3), true );
        game.nextTurn();

        // Turn 1 Demeter
        player = game.getCurrentPlayer();
        actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(4, 1), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(4, 2), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(4, 2), false );
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(3, 2), true );
        game.nextTurn();

        // Turn 2 Pan
        player = game.getCurrentPlayer();
        actions = player.nextStep(Action.start);
        assertTrue(board.checkAction(actions[0], player.getPawn(1), new Coordinate(3, 3)));
        assertTrue(actions[0].execute(board, player.getPawn(1), new Coordinate(3, 3)));     // Pan wins
    }

    @Test
    public void testMinotaurAthenaZeus() throws InvalidActionException {
        Lobby lobby = initLobby("Minotaur", "Athena", "Zeus");

        // Place pawns at starting positions
        lobby.setUpUserPawns(lobby.getUserToSetUp().orElseThrow(), new Coordinate(0, 1), new Coordinate(1, 0));
        lobby.setUpUserPawns(lobby.getUserToSetUp().orElseThrow(), new Coordinate(0, 2), new Coordinate(2, 2));
        lobby.setUpUserPawns(lobby.getUserToSetUp().orElseThrow(), new Coordinate(4, 1), new Coordinate(4, 2));
        assertTrue(lobby.isGameReady());

        Game game = lobby.getGame();
        Board board = game.getBoard();

        // Setup
        board.buildBlock(new Coordinate(4,1));
        board.buildBlock(new Coordinate(4,2));

        // Turn 1 Minotaur
        Player player = game.getCurrentPlayer();
        Action[] actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(0, 2), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(1, 1), true );
        game.nextTurn();

        // Turn 1 Athena
        player = game.getCurrentPlayer();
        assertEquals(player.getPawn(0).getPosition(), new Coordinate(0, 3));
        actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(1, 3), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(1, 2), true );
        game.nextTurn();

        // Turn 1 Zeus
        player = game.getCurrentPlayer();
        actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(4, 3), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(4, 2), true );
        game.nextTurn();

        // Turn 2 Minotaur
        player = game.getCurrentPlayer();
        actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(1, 1), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(0, 0), true );
        game.nextTurn();

        // Turn 2 Athena
        player = game.getCurrentPlayer();
        actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(1, 2), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(0, 3), true );
        game.nextTurn();

        // Turn 2 Zeus
        player = game.getCurrentPlayer();
        actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(4, 2), false );
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(4, 4), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(4, 4), true );
        game.nextTurn();

        // Turn 3 Minotaur
        player = game.getCurrentPlayer();
        actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(0, 0), false );
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(1, 2), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(0, 1), true );
        game.nextTurn();

        // Turn 3 Athena
        player = game.getCurrentPlayer();
        assertEquals(player.getPawn(0).getPosition(), new Coordinate(1, 3));
        actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(3, 3), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(4, 3), true );
        game.nextTurn();

        // Turn 3 Zeus
        player = game.getCurrentPlayer();
        actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(4, 3), true );

        actions = player.nextStep(actions[0]);
        assertTrue(board.checkAction(actions[0], player.getPawn(1), new Coordinate(4, 3)));
        assertFalse(actions[0].execute(board, player.getPawn(1), new Coordinate(4, 3)));
        game.nextTurn();
    }

    @Test
    public void testApolloHephaestusHestia() throws InvalidActionException {
        Lobby lobby = initLobby("Apollo", "Hephaestus", "Hestia");

        // Place pawns at starting positions
        lobby.setUpUserPawns(lobby.getUserToSetUp().orElseThrow(), new Coordinate(0, 0), new Coordinate(1, 1));
        lobby.setUpUserPawns(lobby.getUserToSetUp().orElseThrow(), new Coordinate(0, 1), new Coordinate(3, 3));
        lobby.setUpUserPawns(lobby.getUserToSetUp().orElseThrow(), new Coordinate(0, 4), new Coordinate(4, 1));

        assertTrue(lobby.isGameReady());

        Game game = lobby.getGame();
        Board board = game.getBoard();

        // Turn 1 Apollo
        Player player = game.getCurrentPlayer();
        Action[] actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(1, 1), false );
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(0, 1), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(1, 0), true );
        game.nextTurn();

        // Turn 1 Hephaestus
        player = game.getCurrentPlayer();
        actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(1, 0), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(2, 0), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(2, 1), false );
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(2, 0), true );
        game.nextTurn();

        // Turn 1 Hestia
        player = game.getCurrentPlayer();
        actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(4, 2), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(3, 2), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(4, 3), false );
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(3, 2), true );
        game.nextTurn();

        // Turn 2 Apollo
        player = game.getCurrentPlayer();
        actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(1, 0), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(0, 0), true );
        game.nextTurn();

        // Turn 2 Hephaestus
        player = game.getCurrentPlayer();
        actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(2, 2), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(2, 1), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(1, 1), false );
        checkExec(board, actions[1], player.getPawn(0), new Coordinate(1, 1), true );
        game.nextTurn();

        // Turn 2 Hestia
        player = game.getCurrentPlayer();
        actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(4, 3), true );

        actions = player.nextStep(actions[0]);
        assertTrue(board.checkAction(actions[0], player.getPawn(1), new Coordinate(4, 4)));
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(3, 2), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(4, 4), false );
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(3, 4), false );
        assertTrue(board.checkAction(actions[1], player.getPawn(1), new Coordinate(3, 2)));
        checkExec(board, actions[2], player.getPawn(1), new Coordinate(3, 2), true );
        game.nextTurn();
    }

    @Test
    public void testArtemisPrometheusCharon() throws InvalidActionException {
        Lobby lobby = initLobby("Artemis", "Prometheus", "Charon");

        // Place pawns at starting positions
        lobby.setUpUserPawns(lobby.getUserToSetUp().orElseThrow(), new Coordinate(0, 4), new Coordinate(1, 2));
        lobby.setUpUserPawns(lobby.getUserToSetUp().orElseThrow(), new Coordinate(2, 0), new Coordinate(1, 1));
        lobby.setUpUserPawns(lobby.getUserToSetUp().orElseThrow(), new Coordinate(2, 1), new Coordinate(3, 2));

        assertTrue(lobby.isGameReady());

        Game game = lobby.getGame();
        Board board = game.getBoard();

        // Turn 1 Artemis
        Player player = game.getCurrentPlayer();
        Action[] actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(1, 4), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(0, 4), false );
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(2, 4), true );
        game.nextTurn();

        // Turn 1 Prometheus
        player = game.getCurrentPlayer();
        actions = player.nextStep(Action.start);
        checkExec(board, actions[1], player.getPawn(0), new Coordinate(3, 1), true );

        actions = player.nextStep(actions[1]);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(1, 3), false );
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(3, 0), true );

        actions = player.nextStep(actions[0]);
        assertTrue(board.checkAction(actions[0], player.getPawn(0), new Coordinate(3, 1)));
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(4, 0), true );
        game.nextTurn();

        // Turn 1 Charon
        player = game.getCurrentPlayer();
        actions = player.nextStep(Action.start);
        checkExec(board, actions[1], player.getPawn(0), new Coordinate(3, 0), false );
        checkExec(board, actions[1], player.getPawn(0), new Coordinate(1, 2), false );
        checkExec(board, actions[1], player.getPawn(0), new Coordinate(1, 1), true );

        actions = player.nextStep(actions[1]);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(2, 0), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(1, 0), true );
        game.nextTurn();
    }

    @Test
    public void testUraniaLimusHades() throws InvalidActionException {
        Lobby lobby = initLobby("Urania", "Hades", "Limus");

        // Place pawns at starting positions
        lobby.setUpUserPawns(lobby.getUserToSetUp().orElseThrow(), new Coordinate(0, 3), new Coordinate(3, 2));
        lobby.setUpUserPawns(lobby.getUserToSetUp().orElseThrow(), new Coordinate(0, 2), new Coordinate(1, 2));
        lobby.setUpUserPawns(lobby.getUserToSetUp().orElseThrow(), new Coordinate(2, 1), new Coordinate(3, 1));

        assertTrue(lobby.isGameReady());

        Game game = lobby.getGame();
        Board board = game.getBoard();

        // Turn 1 Urania
        Player player = game.getCurrentPlayer();
        Action[] actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(0, 4), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(0, 0), true );
        game.nextTurn();

        // Turn 1 Hades
        player = game.getCurrentPlayer();
        actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(0, 1), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(0, 0), true );
        game.nextTurn();

        // Turn 1 Limus
        player = game.getCurrentPlayer();
        actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(1, 1), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(0, 0), true );
        game.nextTurn();

        // Turn 2 Urania
        player = game.getCurrentPlayer();
        actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(4, 4), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(4, 3), true );
        game.nextTurn();

        // Turn 2 Hades
        player = game.getCurrentPlayer();
        actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(1, 0), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(0, 1), false );
        checkExec(board, actions[1], player.getPawn(0), new Coordinate(0, 0), true );
        game.nextTurn();

        // Turn 2 Limus
        player = game.getCurrentPlayer();
        actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(2, 1), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(2, 2), true );
        game.nextTurn();

        // Turn 3 Urania
        player = game.getCurrentPlayer();
        actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(2, 2), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(2, 3), true );
        game.nextTurn();

        // Turn 3 Hades
        player = game.getCurrentPlayer();
        actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(1, 3), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(1, 4), true );
        game.nextTurn();

        // Turn 3 Limus
        player = game.getCurrentPlayer();
        actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(2, 0), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(3, 0), true );
        game.nextTurn();

        // Turn 4 Urania
        player = game.getCurrentPlayer();
        actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(2, 1), false );
    }

    @Test
    public void testChronus() throws InvalidActionException{
        Lobby lobby = initLobby("Chronus", "Atlas", "Pan");

        // Place pawns at starting positions
        lobby.setUpUserPawns(lobby.getUserToSetUp().orElseThrow(), new Coordinate(4, 4), new Coordinate(3, 3));
        lobby.setUpUserPawns(lobby.getUserToSetUp().orElseThrow(), new Coordinate(0, 1), new Coordinate(0, 2));
        lobby.setUpUserPawns(lobby.getUserToSetUp().orElseThrow(), new Coordinate(0, 0), new Coordinate(4, 1));

        assertTrue(lobby.isGameReady());

        Game game = lobby.getGame();
        Board board = game.getBoard();

        // Setup
        board.buildBlock(new Coordinate(0,4));
        board.buildBlock(new Coordinate(0,4));
        board.buildBlock(new Coordinate(0,4));
        board.buildDome(new Coordinate(0,4));
        board.buildBlock(new Coordinate(1,4));
        board.buildBlock(new Coordinate(1,4));
        board.buildBlock(new Coordinate(1,4));
        board.buildDome(new Coordinate(1,4));
        board.buildBlock(new Coordinate(2,4));
        board.buildBlock(new Coordinate(2,4));
        board.buildBlock(new Coordinate(2,4));
        board.buildDome(new Coordinate(2,4));
        board.buildBlock(new Coordinate(3,4));
        board.buildBlock(new Coordinate(3,4));
        board.buildBlock(new Coordinate(3,4));
        board.buildDome(new Coordinate(3,4));
        board.buildBlock(new Coordinate(4,3));
        board.buildBlock(new Coordinate(4,3));

        // Turn 1 Chronus
        Player player = game.getCurrentPlayer();
        Action[] actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(3, 2), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(4, 3), true );
        game.nextTurn();

        // Turn 1 Atlas
        player = game.getCurrentPlayer();
        actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(0), new Coordinate(1, 1), true );

        actions = player.nextStep(actions[0]);
        assertTrue(board.checkAction(actions[1], player.getPawn(0), new Coordinate(2, 2)));
        assertFalse(actions[1].execute(board, player.getPawn(0), new Coordinate(2, 2)));
        game.nextTurn();

        // Turn 1 Pan
        player = game.getCurrentPlayer();
        actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(4, 2), true );

        actions = player.nextStep(actions[0]);
        checkExec(board, actions[1], player.getPawn(1), new Coordinate(4, 3), false );
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(4, 1), true );
        game.nextTurn();

        // Turn 2 Chronus
        player = game.getCurrentPlayer();
        actions = player.nextStep(Action.start);
        checkExec(board, actions[0], player.getPawn(1), new Coordinate(3, 3), true );

        actions = player.nextStep(actions[0]);
        assertTrue(board.checkAction(actions[1], player.getPawn(1), new Coordinate(4, 3)));
        assertTrue(actions[1].execute(board, player.getPawn(1), new Coordinate(4, 3)));         //Chronus wins
    }
}