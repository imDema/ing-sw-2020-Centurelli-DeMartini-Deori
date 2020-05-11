package it.polimi.ingsw.serialization;

import com.google.gson.*;
import it.polimi.ingsw.model.action.Checks;
import it.polimi.ingsw.model.action.Check;

import java.lang.reflect.Type;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;

class CheckDeserializer implements JsonDeserializer<Check> {

    enum CheckId {
        NOT_OCCUPIED,
        NEIGHBOUR,
        MAX_ONE_LEVEL_ABOVE,
        MAX_SAME_LEVEL,
        NO_DOME,
        MAX_LEVEL,
        NOT_MAX_LEVEL,
        MIN_LEVEL_ONE,
        CAN_PUSH
    }

    private final Map<CheckId, Check> map = Map.ofEntries(
            new SimpleImmutableEntry<>(CheckId.NOT_OCCUPIED, Checks.notOccupied),
            new SimpleImmutableEntry<>(CheckId.NEIGHBOUR, Checks.neighbour),
            new SimpleImmutableEntry<>(CheckId.MAX_ONE_LEVEL_ABOVE, Checks.maxOneLevelAbove),
            new SimpleImmutableEntry<>(CheckId.MAX_SAME_LEVEL, Checks.maxSameLevel),
            new SimpleImmutableEntry<>(CheckId.NO_DOME, Checks.noDome),
            new SimpleImmutableEntry<>(CheckId.MAX_LEVEL, Checks.maxLevel),
            new SimpleImmutableEntry<>(CheckId.NOT_MAX_LEVEL, Checks.notMaxLevel),
            new SimpleImmutableEntry<>(CheckId.MIN_LEVEL_ONE, Checks.minLevelOne),
            new SimpleImmutableEntry<>(CheckId.CAN_PUSH, Checks.canPush)
    );

    private Check getCheckAllowedFromId(CheckId id) {
        Check check = map.get(id);
        if (check != null)
            return check;

        throw new IllegalStateException();
    }

    @Override
    public Check deserialize(JsonElement jsonElement, Type type,
                             JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        CheckId id = jsonDeserializationContext.deserialize(jsonElement, CheckId.class);
        return getCheckAllowedFromId(id);
    }
}