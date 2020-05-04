package it.polimi.ingsw.model.player.turnsequence;

import it.polimi.ingsw.model.action.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LinearTurnSequenceTest {
    @Test
    public void testSequence() {
        final Action[] first = new Action[] { new Action("Move", ActionFamily.MOVE,
                new Effect[] {Effects.move},
                new Check[] {
                        Checks.neighbour,
                        Checks.maxOneLevelAbove,
                        Checks.notOccupied,
                        Checks.noDome}) };

        final Action[] second = new Action[] { new Action("BuildBlock", ActionFamily.BUILD,
                new Effect[] {Effects.buildBlock},
                new Check[] {
                        Checks.neighbour,
                        Checks.noDome,
                        Checks.notOccupied,
                        Checks.notMaxLevel}) };

        StepSequence seq = new StepSequenceBuilder()
                .addStep(first)
                .addStep(second)
                .build();

        TurnSequence t = new LinearTurnSequence(seq);
        t.start();
        assertArrayEquals(t.getStep(), first);

        t.nextStep(first[0]);
        assertArrayEquals(t.getStep(), second);

        t.nextStep(second[0]);
        assertArrayEquals(t.getStep(), new Action[] {Action.endTurn});

        t.start();
        assertArrayEquals(t.getStep(), first);

        t.nextStep(first[0]);
        assertArrayEquals(t.getStep(), second);

        t.start();
        assertArrayEquals(t.getStep(), first);
    }
}