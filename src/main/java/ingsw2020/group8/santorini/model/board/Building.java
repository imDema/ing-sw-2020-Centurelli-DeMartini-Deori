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

    public void buildDome() throws InvalidBuildException {throw new NotImplementedException();}
    public void buildBlock() throws InvalidBuildException {throw new NotImplementedException();}
}
