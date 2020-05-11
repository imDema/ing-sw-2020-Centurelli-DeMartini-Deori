package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.events.ServerEventsListener;
import it.polimi.ingsw.controller.messages.ActionIdentifier;
import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Building;
import it.polimi.ingsw.model.board.Coordinate;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameControllerTest {
    List<GodIdentifier> availableGods = null;
    List<Coordinate> coordinateList = new ArrayList<>();

    private void initCoordinateList(){
        coordinateList.add(new Coordinate(1, 1));
        coordinateList.add(new Coordinate(2, 1));
        coordinateList.add(new Coordinate(3, 1));
        coordinateList.add(new Coordinate(2, 2));
        coordinateList.add(new Coordinate(3, 2));
        coordinateList.add(new Coordinate(4, 2));
    }
    @Test
    public void testControllerSequence() {
        GameController gameController = new GameController();
        gameController.addServerEventsListener(new ServerEventsListener() {
            @Override
            public void onGodsAvailable(List<GodIdentifier> gods) {
                availableGods = gods;
            }

            @Override
            public void onRequestPlacePawns(User user) {
                Coordinate c1 = coordinateList.remove(0);
                Coordinate c2 = coordinateList.remove(0);
                gameController.onPlacePawns(user, c1, c2);
            }

            @Override
            public void onActionsReady(User user, List<ActionIdentifier> actions) {}
            @Override
            public void onElimination(User user) {}
            @Override
            public void onGodChosen(User user, GodIdentifier godIdentifier) {}
            @Override
            public void onServerError(String type, String description) {}
            @Override
            public void onTurnChange(User currentUser, int turn) {}
            @Override
            public void onUserJoined(User user) {}
            @Override
            public void onWin(User user) {}
            @Override
            public void onBuild(Building building, Coordinate coordinate) {}
            @Override
            public void onMove(Coordinate from, Coordinate to) {}
        });

        gameController.initLobby();
        User u1 = new User("User1");
        User u2 = new User("User2");
        User u3 = new User("User3");
        User u4 = new User("User4");


        assertTrue(gameController.onAddUser(u1));
        assertTrue(gameController.onAddUser(u2));
        assertFalse(gameController.onAddUser(u2));
        assertTrue(gameController.onAddUser(u3));
        assertFalse(gameController.onAddUser(u4));

        assertTrue(gameController.onChooseGod(u1, availableGods.get(0)));
        assertFalse(gameController.onChooseGod(u1, availableGods.get(0)));

        GodIdentifier duplicate = availableGods.get(0);
        assertTrue(gameController.onChooseGod(u2, duplicate));
        assertFalse(gameController.onChooseGod(u3,duplicate));

        initCoordinateList();
        gameController.onChooseGod(u3, availableGods.get(0));

    }
}