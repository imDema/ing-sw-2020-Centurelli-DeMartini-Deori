package it.polimi.ingsw.model;

import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.action.ActionFamily;
import it.polimi.ingsw.model.action.Check;
import it.polimi.ingsw.model.action.Effect;
import it.polimi.ingsw.model.player.God;
import it.polimi.ingsw.model.player.turnsequence.DefaultTurnSequence;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;



class
GameTest {

    @Test
    public void testElimination() throws InvalidStateException {
        Game game = new Game();
        Effect[] effect = new Effect[0];
        Check[] check = new Check[0];
        Action action = new Action("Prov", ActionFamily.MOVE, effect, check);
        DefaultTurnSequence turnSequence = new DefaultTurnSequence(action, action);

        String god_name1 = "God_Name1";
        String god_name2 = "God_Name2";
        String god_name3 = "God_Name3";

        God god1 = new God("A", turnSequence);
        God god2 = new God("B", turnSequence);
        God god3 = new God("C", turnSequence);

        game.addUsername(god_name1);
        game.addUsername(god_name2);
        game.addUsername(god_name3);

        game.setPlayer(god1);
        game.setPlayer(god2);
        game.setPlayer(god3);

        game.nextTurn();
        assertEquals(2,game.getTurn());
        assertEquals(3, game.getPlayerNumber());

        game.elimination(game.getPlayer(1));
        assertEquals(2, game.getPlayerNumber());

        game.elimination(game.getCurrentPlayer());
        assertEquals(1, game.getPlayerNumber());
    }
}