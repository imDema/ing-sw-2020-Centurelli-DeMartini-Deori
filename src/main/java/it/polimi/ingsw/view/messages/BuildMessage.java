package it.polimi.ingsw.view.messages;

import it.polimi.ingsw.model.board.Building;
import it.polimi.ingsw.model.board.Coordinate;

public class BuildMessage implements Message {
    private final Building building;
    private final Coordinate coordinate;

    public BuildMessage(Building building, Coordinate coordinate) {
        this.building = building;
        this.coordinate = coordinate;
    }

    public Building getBuilding() {
        return building;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public MessageId getSerializationId() {
        return MessageId.BUILD;
    }
}
