package it.polimi.ingsw.model.board;

/**
 * Represents the different levels that a {@link Building} can be. Starting from LEVEL0: no blocks to LEVEL3: 3 blocks.
 */
public enum BuildingLevel {
    LEVEL0(0),
    LEVEL1(1),
    LEVEL2(2),
    LEVEL3(3);

    public final int height;

    BuildingLevel(int h) {
        this.height = h;
    }
}
