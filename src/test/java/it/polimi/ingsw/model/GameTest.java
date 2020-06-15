package it.polimi.ingsw.model;

import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GameTest {

    @Test
    public void testElimination() throws IllegalStateException {
        String username1 = "Name1";
        String username2 = "Name2";
        String username3 = "Name3";

        Player player1 = new Player(username1, null);
        Player player2 = new Player(username2, null);
        Player player3 = new Player(username3, null);

        Game game = new Game();
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);

        assertEquals(3, game.getPlayerNumber());
        assertEquals(game.getCurrentPlayer(), player1);

        game.nextTurn();
        assertEquals(1, game.getTurn());
        assertEquals(game.getCurrentPlayer(), player2);

        game.elimination(game.getPlayers().get(1));
        assertEquals(2, game.getPlayerNumber());

        game.elimination(game.getCurrentPlayer());
        assertEquals(1, game.getPlayerNumber());
    }
}