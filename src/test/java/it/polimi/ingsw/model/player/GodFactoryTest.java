package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.action.*;
import it.polimi.ingsw.model.player.turnsequence.DefaultTurnSequence;
import it.polimi.ingsw.model.player.turnsequence.TurnSequence;
import it.polimi.ingsw.model.serialization.Serializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Console;
import java.util.Arrays;
import java.util.stream.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class GodFactoryTest {
    @Test
    public void testDeserializeGod() throws IOException {
        final Action move = new Action("Move",
                new Effect[] {Effects.move},
                new Check[] {
                        Checks.neighbour,
                        Checks.maxOneLevelAbove,
                        Checks.notOccupied,
                        Checks.noDome});

        final Action build = new Action("BuildBlock",
                new Effect[] {Effects.buildBlock},
                new Check[] {
                        Checks.neighbour,
                        Checks.noDome,
                        Checks.notOccupied,
                        Checks.notMaxLevel});

        TurnSequence ts = new DefaultTurnSequence(move, build);
        God expected = new God("Simple", ts);



        // Load test resource into string
        String testDeserialize = Files.lines(Paths.get("src/test/resources/single-god.json"))
                    .reduce("", (str, sub) -> str + sub);

        God actual = Serializer.getGod(testDeserialize);

        assertEquals(expected, actual);
    }

    @Test
    public void testLoadGodsFromResource() {
        GodFactory factory = new GodFactory();
        God[] gods = assertDoesNotThrow(factory::getGods);
        assertTrue(gods.length > 0);

        Arrays.stream(gods)
                .peek(Assertions::assertNotNull)
                .map(God::toString)
                .forEach(System.out::println);
    }
}