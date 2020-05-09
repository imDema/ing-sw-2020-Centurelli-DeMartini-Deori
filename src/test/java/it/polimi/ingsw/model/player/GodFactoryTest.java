package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.action.*;
import it.polimi.ingsw.model.player.turnsequence.LinearTurnSequence;
import it.polimi.ingsw.model.player.turnsequence.StepSequence;
import it.polimi.ingsw.model.player.turnsequence.StepSequenceBuilder;
import it.polimi.ingsw.model.player.turnsequence.TurnSequence;
import it.polimi.ingsw.serialization.Serializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GodFactoryTest {
    @Test
    public void testDeserializeGod() throws IOException {
        final Action move = new Action("Move", ActionFamily.MOVE,
                new Effect[] {Effects.move},
                new Check[] {
                        Checks.neighbour,
                        Checks.maxOneLevelAbove,
                        Checks.notOccupied,
                        Checks.noDome});

        final Action build = new Action("BuildBlock", ActionFamily.BUILD,
                new Effect[] {Effects.buildBlock},
                new Check[] {
                        Checks.neighbour,
                        Checks.noDome,
                        Checks.notOccupied,
                        Checks.notMaxLevel});

        StepSequence seq = new StepSequenceBuilder()
                .addStep(new Action[] {move})
                .addStep(new Action[] {build})
                .build();

        TurnSequence ts = new LinearTurnSequence(seq);
        God expected = new God("Simple", "Description...", ts);


        // Load test resource into string
        String testDeserialize = Files.lines(Paths.get("src/test/resources/single-god.json"))
                    .reduce("", (str, sub) -> str + sub);

        God actual = Serializer.deserializeGod(testDeserialize);

        assertEquals(expected, actual);
    }

    @Test
    public void testLoadGodsFromResource() {
        GodFactory factory = new GodFactory();
        List<God> gods = assertDoesNotThrow(factory::getGods);
        assertTrue(gods.size() > 0);

        gods.stream()
                //.peek(System.out::println)
                .forEach(Assertions::assertNotNull);
    }
}