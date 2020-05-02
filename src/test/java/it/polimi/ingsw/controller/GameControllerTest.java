package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.messages.GodIdentifier;
import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Coordinate;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameControllerTest {
    List<GodIdentifier> gods = null;
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
        gameController.setGodsAvailableListener(g -> gods = g);
        gameController.initLobby();
        User u1 = gameController.onAddUser("User1").orElseThrow();
        User u2 = gameController.onAddUser("user2").orElseThrow();
        assertTrue(gameController.onAddUser("User1").isEmpty());
        User u3 = gameController.onAddUser("user3").orElseThrow();
        assertTrue(gameController.onAddUser("User4").isEmpty());

        assertTrue(gameController.onChooseGod(u1, gods.get(0)));
        assertFalse(gameController.onChooseGod(u1, gods.get(0)));

        GodIdentifier duplicate = gods.get(0);
        assertTrue(gameController.onChooseGod(u2, duplicate));
        assertFalse(gameController.onChooseGod(u3,duplicate));

        initCoordinateList();
        gameController.setRequestPlacePawnsListener(user1 -> {
            Coordinate c1 = coordinateList.remove(0);
            Coordinate c2 = coordinateList.remove(0);
            gameController.onPlacePawns(user1, c1, c2);
        });
        gameController.onChooseGod(u3, gods.get(0));

    }
}