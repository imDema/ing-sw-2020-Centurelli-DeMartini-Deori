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
    public void testSequence() {
        Supplier<StepSequence> common = () -> new StepSequenceBuilder()
                .addStep(new Action[] { move }).build();
        Supplier<StepSequence> branchOne = () -> new StepSequenceBuilder()
                .addStep(new Action[] {move})
                .addStep(new Action[] {buildBlock, buildDome}).build();

        Supplier<StepSequence> branchTwo = () -> new StepSequenceBuilder()
                .addStep(new Action[] {buildBlock, buildDome})
                .addStep(new Action[] {Action.endTurn}).build();

        StepSequence testCommon = common.get();
        StepSequence testBranchOne = branchOne.get();
        StepSequence testBranchTwo = branchTwo.get();

        TurnSequence t = new BranchingTurnSequence(common.get(), branchOne.get(), branchTwo.get());
        t.start();
        Action[] step = testCommon.next().orElseThrow();
        assertArrayEquals(step, t.getStep());

        t.nextStep(step[0]);

        step = t.getStep();
        // Assert second step contains combination of the two branches
        for(Action a : testBranchOne.peek().orElseThrow()) {
            assertTrue(Arrays.asList(step).contains(a));
        }
        for(Action a : testBranchTwo.peek().orElseThrow()) {
            assertTrue(Arrays.asList(step).contains(a));
        }

        t.nextStep(testBranchTwo.next().orElseThrow()[1]);
        assertArrayEquals(testBranchTwo.next().orElseThrow(), t.getStep());

        t.start();
        testCommon.start();
        testBranchOne.start();
        testBranchTwo.start();

        t.nextStep(testCommon.next().orElseThrow()[0]);
        t.nextStep(testBranchOne.next().orElseThrow()[0]);
        assertArrayEquals(testBranchOne.peek().orElseThrow(), t.getStep());
        t.nextStep(testBranchOne.next().orElseThrow()[0]);
        assertArrayEquals(new Action[] {Action.endTurn}, t.getStep());
    }
}