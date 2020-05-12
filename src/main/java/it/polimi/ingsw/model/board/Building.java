package it.polimi.ingsw.model.board;

public class Building {
    private BuildingLevel level = BuildingLevel.LEVEL0;
    private boolean dome = false;

    public BuildingLevel getLevel() {
        return level;
    }

    public boolean hasDome() {
        return dome;
    }

    public void buildDome() throws InvalidActionException {
        if (!dome) {
            dome = true;
        } else {
            throw new InvalidActionException();
        }
    }

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

    // returns the level difference of b relative to this
    public int getLevelDifference(Building b) {
        return b.level.height - this.level.height;
    }
}
