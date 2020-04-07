package it.polimi.ingsw.model.board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BuildingTest {
    @Test
    public void testBuildDome() {
        Building b = new Building();

        assertFalse(b.hasDome());

        try {
            b.buildDome();
        } catch (InvalidActionException e) {
            e.printStackTrace();
            fail(); //Shouldn't throw
        }

        assertTrue(b.hasDome());

        assertThrows(InvalidActionException.class, b::buildDome);
    }

    @Test
    public void testBuildBlock() throws InvalidActionException {
        Building b = new Building();
        assertEquals(BuildingLevel.LEVEL0, b.getLevel());

        b.buildBlock();
        assertEquals(BuildingLevel.LEVEL1, b.getLevel());

        b.buildBlock();
        assertEquals(BuildingLevel.LEVEL2, b.getLevel());

        b.buildBlock();
        assertEquals(BuildingLevel.LEVEL3, b.getLevel());

        assertThrows(InvalidActionException.class, b::buildBlock);
    }
}