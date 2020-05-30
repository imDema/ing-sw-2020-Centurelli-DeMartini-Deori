package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.OnServerEventListener;
import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Building;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.model.board.InvalidActionException;
import it.polimi.ingsw.model.player.God;
import it.polimi.ingsw.view.messages.MessageDispatcher;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {
    List<GodIdentifier> availableGods = null;
    List<Coordinate> coordinateList = new ArrayList<>();
    List<God> gods = new ArrayList<>();
    Map<User, GodIdentifier> usersGods = new HashMap<>();
    List<ActionIdentifier> availableActions = new ArrayList<>();
    boolean flag = false;
    boolean gameEnded = false;

    private void initCoordinateList(){
        coordinateList.add(new Coordinate(2, 1));
        coordinateList.add(new Coordinate(1, 3));
        coordinateList.add(new Coordinate(0, 0));
        coordinateList.add(new Coordinate(0, 1));
        coordinateList.add(new Coordinate(4, 2));
        coordinateList.add(new Coordinate(3, 3));
    }
    @Test
    public void testControllerSequence() throws InvalidActionException {
        GameController gameController = new GameController();
        MessageDispatcher dispatcher = new MessageDispatcher();
        gameController.addServerEventsListener(new OnServerEventListener() {


            @Override
            public void onSizeSelected(int size) {
                dispatcher.onSizeSelected(size);
            }

            @Override
            public void onGodsAvailable(List<GodIdentifier> gods) {
                dispatcher.onGodsAvailable(gods);
            }

            @Override
            public void onRequestPlacePawns(User user) {
                dispatcher.onRequestPlacePawns(user);
            }

            @Override
            public void onPawnPlaced(User owner, int pawnId, Coordinate coordinate) {
                dispatcher.onPawnPlaced(owner, pawnId, coordinate);
            }

            @Override
            public void onActionsReady(User user, List<ActionIdentifier> actions) {
                dispatcher.onActionsReady(user, actions);
            }

            @Override
            public void onElimination(User user) {
                dispatcher.onElimination(user);
            }

            @Override
            public void onGodChosen(User user, GodIdentifier godIdentifier) {
                dispatcher.onGodChosen(user, godIdentifier);
            }

            @Override
            public void onServerError(String type, String description) {}

            @Override
            public void onTurnChange(User currentUser, int turn) {
                dispatcher.onTurnChange(currentUser, turn);
            }

            @Override
            public void onUserJoined(User user) {}

            @Override
            public void onWin(User user) {
                dispatcher.onWin(user);
            }

            @Override
            public void onBuild(Building building, Coordinate coordinate) {
                dispatcher.onBuild(building, coordinate);
            }

            @Override
            public void onMove(Coordinate from, Coordinate to) {}
        });

        dispatcher.setOnGodsAvailableListener(gods -> availableGods = gods);

        initCoordinateList();
        dispatcher.setOnRequestPlacePawnsListener(user -> {
            Coordinate c1 = coordinateList.remove(0);
            Coordinate c2 = coordinateList.remove(0);
            gameController.onPlacePawns(user, c1, c2);
        });
        dispatcher.setOnActionsReadyListener((u,l) -> {
            System.out.println("Actions available for user " + u.getUsername());
            availableActions = l;
        });
        dispatcher.setOnGodChosenListener((u,g) -> usersGods.put(u, g));
        dispatcher.setOnTurnChangeListener((u,t) -> System.out.println(u.getUsername()+" turn"));

        gameController.onSelectPlayerNumber(3);
        User u1 = new User("User1");
        User u2 = new User("User2");
        User u3 = new User("User3");
        User u4 = new User("User4");

        assertTrue(gameController.onAddUser(u1));
        assertTrue(gameController.onAddUser(u2));
        assertFalse(gameController.onAddUser(u2));
        assertTrue(gameController.onAddUser(u3));
        assertFalse(gameController.onAddUser(u4));


        List<GodIdentifier> selectedGods = new ArrayList<>();
        selectedGods.add(availableGods.stream().filter(g -> g.getName().equals("Pan")).findFirst().orElseThrow());
        selectedGods.add(availableGods.stream().filter(g -> g.getName().equals("Athena")).findFirst().orElseThrow());

        assertFalse(gameController.onSelectGods(u1, selectedGods));
        selectedGods.add(availableGods.stream().filter(g -> g.getName().equals("Apollo")).findFirst().orElseThrow());

        assertTrue(gameController.onSelectGods(u3, selectedGods));
        assertEquals(3, availableGods.size());

        GodIdentifier pan = availableGods.stream().filter(g -> g.getName().equals("Pan")).findFirst().orElseThrow();
        assertFalse(gameController.onChooseGod(u3, pan));
        assertTrue(gameController.onChooseGod(u1, pan));
        assertFalse(gameController.onChooseGod(u1, pan));

        GodIdentifier athena = availableGods.stream().filter(g -> g.getName().equals("Athena")).findFirst().orElseThrow();
        assertFalse(gameController.onChooseGod(u3, athena));
        assertTrue(gameController.onChooseGod(u2, athena));
        assertFalse(gameController.onChooseGod(u3, athena));

        assertFalse(gameController.onChooseFirstPlayer(u2, u3));
        assertFalse(gameController.onChooseFirstPlayer(u3, u3));
        assertTrue(gameController.onChooseFirstPlayer(u3, u1));

        // Default coordinates set for u1,u2,u3
        // 1) (2-1) (1-3)
        // 2) (0-0) (0-1)
        // 3) (4-2) (3-3)

        Board board = gameController.getBoard();
        board.buildBlock(new Coordinate(1, 0));
        board.buildBlock(new Coordinate(1, 0));
        board.buildBlock(new Coordinate(1, 1));
        board.buildBlock(new Coordinate(1, 2));
        board.buildBlock(new Coordinate(0, 2));
        board.buildBlock(new Coordinate(0, 2));


        gameController.onExecuteAction(u1, 0, availableActions.get(0), new Coordinate(1, 1));
        dispatcher.setOnEliminationListener(user -> {
            if (user.equals(u2))
                flag = true;
        });
        assertFalse(flag);
        gameController.onExecuteAction(u1, 0 , availableActions.get(0), new Coordinate(1, 2));
        assertTrue(flag);
        flag = false;
        dispatcher.setOnEliminationListener(null);

        assertFalse(gameController.onCheckAction(u3, 0, availableActions.get(0), new Coordinate(4, 0)));
        assertFalse(gameController.onExecuteAction(u3, 0, availableActions.get(0), new Coordinate(4, 0)));
        for (ActionIdentifier a : availableActions) {
            System.out.println(a.getDescription());
        }
        assertTrue(gameController.onCheckAction(u3, 0, availableActions.get(0), new Coordinate(4, 1)));
        assertTrue(gameController.onExecuteAction(u3, 0, availableActions.get(0), new Coordinate(4, 1)));

        gameController.onExecuteAction(u3, 0, availableActions.get(0), new Coordinate(4, 0));

        gameController.onExecuteAction(u1, 0, availableActions.get(0), new Coordinate(1, 2));
        gameController.onExecuteAction(u1, 0, availableActions.get(0), new Coordinate(1, 1));

        // u3 move
        gameController.onExecuteAction(u3, 0, availableActions.get(0), new Coordinate(4, 0));
        gameController.onExecuteAction(u3, 0, availableActions.get(0), new Coordinate(4, 1));

        dispatcher.setOnWinListener(u -> {
            flag = true;
            System.out.println(u.getUsername() + " has won the game!");
        });

        gameController.setGameFinishedListener(controller -> gameEnded = true);

        assertFalse(flag);
        assertFalse(gameEnded);
        gameController.onExecuteAction(u1, 0, availableActions.get(0), new Coordinate(2, 2));
        assertTrue(flag);
        assertTrue(gameEnded);


        dispatcher.setOnWinListener(null);
        flag = false;

        /*
        for (ActionIdentifier a : availableActions) {
            System.out.println(a.getDescription());
        }
        board.printBoard();
        */
    }
}