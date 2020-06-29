package it.polimi.ingsw.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import it.polimi.ingsw.model.action.Check;
import it.polimi.ingsw.model.action.Checks;

import java.lang.reflect.Type;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;

/**
 * Json deserialization adapter for {@link Check}
 */
class CheckDeserializer implements JsonDeserializer<Check> {

    enum CheckId {
        NO_ALLY,
        NOT_OCCUPIED,
        NEIGHBOUR,
        NEIGHBOUR_LOOPING,
        MAX_ONE_LEVEL_ABOVE,
        MAX_SAME_LEVEL,
        NO_DOME,
        MAX_LEVEL,
        NOT_MAX_LEVEL,
        NOT_OCCUPIED_OR_SELF,
        NOT_PERIMETER,
        MIN_LEVEL_ONE,
        CAN_PUSH,
        CAN_FERRY,
    }

    private final Map<CheckId, Check> map = Map.ofEntries(
            new SimpleImmutableEntry<>(CheckId.NO_ALLY, Checks.noAlly),
            new SimpleImmutableEntry<>(CheckId.NOT_OCCUPIED, Checks.notOccupied),
            new SimpleImmutableEntry<>(CheckId.NEIGHBOUR, Checks.neighbour),
            new SimpleImmutableEntry<>(CheckId.NEIGHBOUR_LOOPING, Checks.neighbourLooping),
            new SimpleImmutableEntry<>(CheckId.MAX_ONE_LEVEL_ABOVE, Checks.maxOneLevelAbove),
            new SimpleImmutableEntry<>(CheckId.MAX_SAME_LEVEL, Checks.maxSameLevel),
            new SimpleImmutableEntry<>(CheckId.NO_DOME, Checks.noDome),
            new SimpleImmutableEntry<>(CheckId.MAX_LEVEL, Checks.maxLevel),
            new SimpleImmutableEntry<>(CheckId.NOT_MAX_LEVEL, Checks.notMaxLevel),
            new SimpleImmutableEntry<>(CheckId.MIN_LEVEL_ONE, Checks.minLevelOne),
            new SimpleImmutableEntry<>(CheckId.NOT_OCCUPIED_OR_SELF, Checks.notOccupiedOrSelf),
            new SimpleImmutableEntry<>(CheckId.NOT_PERIMETER, Checks.notPerimeter),
            new SimpleImmutableEntry<>(CheckId.CAN_PUSH, Checks.canPush),
            new SimpleImmutableEntry<>(CheckId.CAN_FERRY, Checks.canFerry)
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
