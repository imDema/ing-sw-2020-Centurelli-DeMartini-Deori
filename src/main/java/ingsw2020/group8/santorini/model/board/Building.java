package ingsw2020.group8.santorini.model.board;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Building {
    private BuildingLevel level = BuildingLevel.LEVEL1;
    private boolean dome = false;

    public BuildingLevel getLevel() {
        return level;
    }

    public boolean hasDome() {
        return dome;
    }

    public void buildDome() throws InvalidBuildException {
        if (!dome) {
            dome = true;
        } else {
            throw new InvalidBuildException;
        }
    }
    public void buildBlock() throws InvalidBuildException {
        switch (level){
            case BuildingLevel.LEVEL1:
                level = BuildingLevel.LEVEL2;
                break;
            case BuildingLevel.LEVEL2:
                level = BuildingLevel.LEVEL3;
                break;
            case BuildingLevel.LEVEL3:
                throw new InvalidBuildException;
                break;
        }
    }
}
