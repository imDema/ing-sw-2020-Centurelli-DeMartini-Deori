package ingsw2020.group8.santorini.model.board;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuildingTest {
    @Test
    void buildDome() {
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
    void buildBlock() {
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