package it.polimi.ingsw.model;

import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class LobbyTest {

    @Test
    public void testAddPlayer() {
        Lobby lobby = new Lobby(3);

        String username1 = "Name1";
        String username2 = "Name2";
        String username3 = "Name3";
        String username4 = "Name4";

        Player player1 = new Player(username1, null);
        Player player2 = new Player(username2, null);
        Player player3 = new Player(username3, null);
        Player player4 = new Player(username4, null);

        lobby.addPlayer(player1);
        lobby.addPlayer(player2);
        lobby.addPlayer(player3);

        assertEquals(lobby.getSize(), 3);

        Game game = lobby.createGame();
        assertEquals(game.getPlayerNumber(), lobby.getSize());
        assertThrows(IllegalStateException.class, () -> lobby.addPlayer(player4));
    }
}