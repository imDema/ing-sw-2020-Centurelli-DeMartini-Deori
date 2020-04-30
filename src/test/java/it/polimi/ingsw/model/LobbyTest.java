package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.messages.User;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.model.player.God;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LobbyTest {

    @Test
    public void testIsGameReady() {
        Lobby lobby = new Lobby(3);
        List<God> godList;

        try {
            lobby.loadGods();
        } catch (IOException ex) {
            ex.printStackTrace();
            fail();
            return;
        }


        String username1 = "Name1";
        String username2 = "Name2";
        String username3 = "Name3";
        String username4 = "Name4";

        User user1 = new User(username1);
        User user2 = new User(username2);
        User user3 = new User(username3);
        User user4 = new User(username4);

        Coordinate c1 = new Coordinate(1,2);
        Coordinate c2 = new Coordinate(2,2);
        Coordinate c3 = new Coordinate(2,3);
        Coordinate c4 = new Coordinate(2,1);
        Coordinate c5 = new Coordinate(3,1);
        Coordinate c6 = new Coordinate(3,3);

        assertTrue(lobby.addUser(user1));
        assertFalse(lobby.addUser(user1));
        assertTrue(lobby.addUser(user2));
        assertFalse(lobby.addUser(user2));
        assertTrue(lobby.addUser(user3));
        assertFalse(lobby.addUser(user3));
        assertFalse(lobby.addUser(user4));

        assertEquals(3,lobby.getUserNumber());

        godList = lobby.getAvailableGods();
        lobby.chooseGod(user1, godList.get(0));

        godList = lobby.getAvailableGods();
        lobby.chooseGod(user2, godList.get(1));

        godList = lobby.getAvailableGods();
        lobby.chooseGod(user3, godList.get(2));

        assertTrue(lobby.isGameFull());

        assertTrue(lobby.getUserToSetUp().isPresent());
        assertTrue(lobby.setUpUserPawns(lobby.getUserToSetUp().get(), c1, c2));

        assertFalse(lobby.setUpUserPawns(lobby.getUserToSetUp().get(), c1, c2));

        assertTrue(lobby.getUserToSetUp().isPresent());
        lobby.setUpUserPawns(lobby.getUserToSetUp().get(), c3, c4);

        assertTrue(lobby.getUserToSetUp().isPresent());
        lobby.setUpUserPawns(lobby.getUserToSetUp().get(), c5, c6);

        assertTrue(lobby.isGameReady());
    }
}