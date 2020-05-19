package it.polimi.ingsw.model.board;

/**
 * The Building class represents the abstraction of a Building on the cells of the board
 *  The levels are represented through {@link BuildingLevel}
 * The levels of the buildings in the game starts from LEVEL0 which is
 * the default building level on every coordinate where no one has built yet
 * and the level goes up until LEVEL3 which is the maximum building level in the game.
 * The level is represented by an inner field called BuildingLevel
 * On top of LEVEL3 each building can have a dome, represented by the inner field dome:
 * dome == true if there is the dome
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
     * Build a dome on top of the building on which it's called if there isn't one already
     * @throws InvalidActionException when trying to build a dome when there is already one
     */
    public void buildDome() throws InvalidActionException {
        if (!dome) {
            dome = true;
        } else {
            throw new InvalidActionException();
        }
    }

    /**
     * buildBlock() increase the level of the building on which it's called by 1 every time it's called
     * @throws InvalidActionException if the building has already reached the maximum level (LEVEL3)
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
     */
    public int getLevelDifference(Building b) {
        return b.level.height - this.level.height;
    }
}
