package it.polimi.ingsw.model.board;

/**
 * Model representation of a building on the board.
 * @see BuildingLevel
 */
public class Building {
    private BuildingLevel level = BuildingLevel.LEVEL0;
    private boolean dome = false;

    public BuildingLevel getLevel() {
        return level;
    }

    public boolean hasDome() {
        return dome;
    }

    /**
     * Build a dome on top of the building.
     * @throws InvalidActionException if the building already has a dome
     */
    public void buildDome() throws InvalidActionException {
        if (!dome) {
            dome = true;
        } else {
            throw new InvalidActionException();
        }
    }

    /**
     * Build a new block on the building increasing its {@link BuildingLevel} by one.
     * @throws InvalidActionException if the building is already at the maximum level (LEVEL3)
     */
    public void buildBlock() throws InvalidActionException {
        switch (level) {
            case LEVEL0 -> level = BuildingLevel.LEVEL1;
            case LEVEL1 -> level = BuildingLevel.LEVEL2;
            case LEVEL2 -> level = BuildingLevel.LEVEL3;
            case LEVEL3 -> throw new InvalidActionException();
        }
    }

    @Override
    public String toString() {
        return "Building: Level " + level.height + (dome ? " with dome" : "");
    }

    /**
     * @return the level difference between b and this building relative to this building
     * @param b Building to compare to
     */
    public int getLevelDifference(Building b) {
        return b.level.height - this.level.height;
    }
}
