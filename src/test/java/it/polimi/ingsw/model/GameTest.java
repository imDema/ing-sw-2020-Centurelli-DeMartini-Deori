package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.action.ActionFamily;
import it.polimi.ingsw.model.action.Check;
import it.polimi.ingsw.model.action.Effect;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.turnsequence.DefaultTurnSequence;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GameTest {

    @Test
    public void testElimination() throws IllegalStateException {

        Effect[] effect = new Effect[0];
        Check[] check = new Check[0];
        Action action = new Action("Prov", ActionFamily.MOVE, effect, check);
        DefaultTurnSequence turnSequence = new DefaultTurnSequence(action, action);

        String username1 = "Name1";
        String username2 = "Name2";
        String username3 = "Name3";

        Player player1 = new Player(username1, null);
        Player player2 = new Player(username2, null);
        Player player3 = new Player(username3, null);

        List<Player> playersInGame = new ArrayList<>();
        playersInGame.add(player1);
        playersInGame.add(player2);
        playersInGame.add(player3);
        Game game = new Game(playersInGame);

        assertEquals(3, game.getPlayerNumber());
        assertEquals(game.getCurrentPlayer(), player1);

        game.nextTurn();
        assertEquals(1, game.getTurn());
        assertEquals(game.getCurrentPlayer(), player2);

        game.elimination(game.getPlayer(1));
        assertEquals(2, game.getPlayerNumber());

        game.elimination(game.getCurrentPlayer());
        assertEquals(1, game.getPlayerNumber());
    }
}