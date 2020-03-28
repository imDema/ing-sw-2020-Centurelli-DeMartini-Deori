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
        } catch (InvalidBuildException e) {
            e.printStackTrace();
            fail(); //Shouldn't throw
        }

        assertTrue(b.hasDome());

        assertThrows(InvalidBuildException.class, b::buildDome);
    }

    @Test
    public void testBuildBlock() {
        Building b = new Building();

        assertEquals(BuildingLevel.LEVEL1, b.getLevel());

        try {
            b.buildBlock();
        } catch (InvalidBuildException e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(BuildingLevel.LEVEL2, b.getLevel());

        try {
            b.buildBlock();
        } catch (InvalidBuildException e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(BuildingLevel.LEVEL3, b.getLevel());

        assertThrows(InvalidBuildException.class, b::buildBlock);
    }
}