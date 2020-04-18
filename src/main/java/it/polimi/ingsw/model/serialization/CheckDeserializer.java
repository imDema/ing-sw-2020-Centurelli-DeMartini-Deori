package it.polimi.ingsw.model.serialization;

import com.google.gson.*;
import it.polimi.ingsw.model.action.Checks;
import it.polimi.ingsw.model.action.Check;

import java.lang.reflect.Type;

class CheckDeserializer implements JsonDeserializer<Check> {

    enum CheckId {
        NOT_OCCUPIED,
        NEIGHBOUR,
        MAX_ONE_LEVEL_ABOVE,
        MAX_SAME_LEVEL,
        NO_DOME,
        MAX_LEVEL,
        NOT_MAX_LEVEL,
        CAN_PUSH
    }

    private Check getCheckAllowedFromId(CheckId id) {
        switch (id){
            case NOT_OCCUPIED:
                return Checks.notOccupied;
            case NEIGHBOUR:
                return Checks.neighbour;
            case MAX_ONE_LEVEL_ABOVE:
                return Checks.maxOneLevelAbove;
            case MAX_SAME_LEVEL:
                return Checks.maxSameLevel;
            case NO_DOME:
                return Checks.noDome;
            case MAX_LEVEL:
                return Checks.maxLevel;
            case NOT_MAX_LEVEL:
                return Checks.notMaxLevel;
            case CAN_PUSH:
                return Checks.canPush;
        }

        throw new IllegalStateException();
    }

    @Override
    public Check deserialize(JsonElement jsonElement, Type type,
                             JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        CheckId id = jsonDeserializationContext.deserialize(jsonElement, CheckId.class);
        return getCheckAllowedFromId(id);
    }
}
