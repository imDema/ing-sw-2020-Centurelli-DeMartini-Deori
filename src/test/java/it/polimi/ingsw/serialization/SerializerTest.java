package it.polimi.ingsw.serialization;

import it.polimi.ingsw.Resources;
import it.polimi.ingsw.model.action.*;
import it.polimi.ingsw.model.player.God;
import it.polimi.ingsw.model.player.turnsequence.LinearTurnSequence;
import it.polimi.ingsw.model.player.turnsequence.StepSequence;
import it.polimi.ingsw.model.player.turnsequence.StepSequenceBuilder;
import it.polimi.ingsw.model.player.turnsequence.TurnSequence;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SerializerTest {
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
        String config = Resources.loadGodConfig(this);
        assertTrue(config.length() > 0);

        List<God> gods = Serializer.deserializeGodList(config);
        assertTrue(gods.size() > 0);

        gods.stream()
                //.peek(System.out::println)
                .forEach(Assertions::assertNotNull);
    }
}