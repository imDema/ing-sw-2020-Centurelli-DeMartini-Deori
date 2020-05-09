package it.polimi.ingsw.serialization;

import com.google.gson.*;
import it.polimi.ingsw.model.action.Effects;
import it.polimi.ingsw.model.action.Effect;

import java.lang.reflect.Type;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;

class EffectDeserializer implements JsonDeserializer<Effect> {

    enum EffectId {
        MOVE,
        BUILD_BLOCK,
        BUILD_DOME,
        SWAP_PAWNS,
        PUSH_PAWN,
        FORBID_MOVE_UP,
        FORBID_MOVE_BACK,
        FORBID_COORDINATE,
        FORBID_OTHER_COORDINATES,
        WIN_ON_JUMP_DOWN
    }

    private final Map<EffectId, Effect> map = Map.ofEntries(
        new SimpleImmutableEntry<>(EffectId.MOVE, Effects.move),
        new SimpleImmutableEntry<>(EffectId.BUILD_BLOCK, Effects.buildBlock),
        new SimpleImmutableEntry<>(EffectId.BUILD_DOME, Effects.buildDome),
        new SimpleImmutableEntry<>(EffectId.SWAP_PAWNS, Effects.swapPawns),
        new SimpleImmutableEntry<>(EffectId.PUSH_PAWN, Effects.pushPawn),
        new SimpleImmutableEntry<>(EffectId.FORBID_MOVE_UP, Effects.forbidMoveUp),
        new SimpleImmutableEntry<>(EffectId.FORBID_MOVE_BACK, Effects.forbidMoveBack),
        new SimpleImmutableEntry<>(EffectId.FORBID_COORDINATE, Effects.forbidCoordinate),
        new SimpleImmutableEntry<>(EffectId.FORBID_OTHER_COORDINATES, Effects.forbidOtherCoordinates),
        new SimpleImmutableEntry<>(EffectId.WIN_ON_JUMP_DOWN, Effects.winOnJumpDown)
    );

    private Effect getEffectFromId(EffectId id) {
        Effect effect = map.get(id);
        if (effect != null)
            return effect;

        throw new IllegalStateException();
    }

    @Override
    public Effect deserialize(JsonElement jsonElement, Type type,
                              JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        EffectId id = jsonDeserializationContext.deserialize(jsonElement, EffectId.class);
        return getEffectFromId(id);
    }
}
