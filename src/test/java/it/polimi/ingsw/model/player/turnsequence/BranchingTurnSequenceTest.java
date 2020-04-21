package it.polimi.ingsw.model.player.turnsequence;

import it.polimi.ingsw.model.action.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public class BranchingTurnSequenceTest {
    final Action move = new Action("Move", ActionFamily.MOVE,
            new Effect[] {Effects.move},
            new Check[] {
                    Checks.neighbour,
                    Checks.maxOneLevelAbove,
                    Checks.notOccupied,
                    Checks.noDome});

    final Action buildBlock = new Action("Build Block", ActionFamily.BUILD,
            new Effect[] {Effects.buildBlock},
            new Check[] {
                    Checks.neighbour,
                    Checks.noDome,
                    Checks.notOccupied,
                    Checks.notMaxLevel});

    final Action buildDome = new Action("Build Dome", ActionFamily.BUILD,
            new Effect[] {Effects.buildDome},
            new Check[] {
                    Checks.neighbour,
                    Checks.noDome,
                    Checks.notOccupied,
                    Checks.maxLevel});

    @Test
    private void testSequence() { //TODO
        Supplier<StepSequence> common = () -> new StepSequenceBuilder()
                .addStep(new Action[] { move }).build();
        Supplier<StepSequence> branchOne = () -> new StepSequenceBuilder()
                .addStep(new Action[] {move})
                .addStep(new Action[] {buildBlock, buildDome}).build();

        Supplier<StepSequence> branchTwo = () -> new StepSequenceBuilder()
                .addStep(new Action[] {buildBlock, buildDome})
                .addStep(new Action[] {Action.endTurn}).build();

        TurnSequence t = new BranchingTurnSequence(common.get(), branchOne.get(), branchTwo.get());
        t.start();
        StepSequence comm = common.get();
        assertArrayEquals(comm.next().get(), t.getStep());
//
//        t.nextStep(common[0]);
//
//        Action[] step = t.getStep();
//        // Assert second step contains combination of the two branches
//        for(Action a : branchOne[0]) {
//            assertTrue(Arrays.asList(step).contains(a));
//        }
//        for(Action a : branchTwo[0]) {
//            assertTrue(Arrays.asList(step).contains(a));
//        }
//
//        t.nextStep(branchTwo[0][0]);
//        assertArrayEquals(branchTwo[1], t.getStep());
//
//        t.start();
//        t.nextStep(common[0]);
//        t.nextStep(branchOne[0][0]);
//        assertArrayEquals(branchOne[1], t.getStep());

    }
}