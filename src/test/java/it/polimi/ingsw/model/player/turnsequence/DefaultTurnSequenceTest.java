package it.polimi.ingsw.model.player.turnsequence;

import it.polimi.ingsw.model.action.*;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Coordinate;
import it.polimi.ingsw.model.player.Pawn;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import static java.util.Collections.singletonList;

class DefaultTurnSequenceTest {

    private boolean noEffect(Board b, Pawn p, Coordinate c) {
        return true;
    }

    @Test
    public void testSequence() {
        final Action first = new Action("Move", ActionFamily.MOVE,
                new Effect[] {Effects.move},
                new Check[] {
                        Checks.neighbour,
                        Checks.maxOneLevelAbove,
                        Checks.notOccupied,
                        Checks.noDome});

        final Action second = new Action("BuildBlock", ActionFamily.BUILD,
                new Effect[] {Effects.buildBlock},
                new Check[] {
                        Checks.neighbour,
                        Checks.noDome,
                        Checks.notOccupied,
                        Checks.notMaxLevel});

        TurnSequence t = new DefaultTurnSequence(first, second);
        assertArrayEquals(t.getStep().toArray(), singletonList(first).toArray());

        t.nextStep(first);
        assertArrayEquals(t.getStep().toArray(), singletonList(second).toArray());

        t.nextStep(second);
        assertArrayEquals(t.getStep().toArray(), singletonList(Action.endTurn).toArray());

        t.start();
        assertArrayEquals(t.getStep().toArray(), singletonList(first).toArray());

        t.nextStep(first);
        assertArrayEquals(t.getStep().toArray(), singletonList(second).toArray());

        t.start();
        assertArrayEquals(t.getStep().toArray(), singletonList(first).toArray());
    }
}