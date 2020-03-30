package it.polimi.ingsw.model.board;

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
            throw new InvalidBuildException();
        }
    }
    public void buildBlock() throws InvalidBuildException {
        switch (level){
            case LEVEL1:
                level = BuildingLevel.LEVEL2;
                break;
            case LEVEL2:
                level = BuildingLevel.LEVEL3;
                break;
            case LEVEL3:
                throw new InvalidBuildException();
        }
    }

    // return the difference between building1 and building2 in the following way:
    // difference = building1.level - building2.level
    public static int getLevelDifference(Building building1, Building building2) {
        switch (building1.level){
            case LEVEL1:
                switch (building2.level){
                    case LEVEL1:
                        return 0;
                    case LEVEL2:
                        return -1;
                    case LEVEL3:
                        return -2;
                }
            case LEVEL2:
                switch (building2.level){
                    case LEVEL1:
                        return 1;
                    case LEVEL2:
                        return 0;
                    case LEVEL3:
                        return -1;
                }
            case LEVEL3:
                switch (building2.level){
                    case LEVEL1:
                        return 2;
                    case LEVEL2:
                        return 1;
                    case LEVEL3:
                        return 0;
                }
            default:
                return -3;
        }
    }
}
