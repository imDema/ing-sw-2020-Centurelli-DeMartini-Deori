package it.polimi.ingsw.model.serialization;

import com.google.gson.*;
import it.polimi.ingsw.model.action.Effects;
import it.polimi.ingsw.model.action.Effect;

import java.lang.reflect.Type;

class EffectDeserializer implements JsonDeserializer<Effect> {

    public enum EffectId {
        MOVE,
        BUILD_BLOCK,
        BUILD_DOME,
        SWAP_PAWNS,
        PUSH_PAWN,
        FORBID_MOVE_UP,
        FORBID_CURRENT_COORDINATE,
        FORBID_TARGET_COORDINATE
    }

    private Effect getEffectFromId(EffectId id) {
        switch (id){
            case MOVE:
                return Effects.move;
            case BUILD_BLOCK:
                return Effects.buildBlock;
            case BUILD_DOME:
                return Effects.buildDome;
            case SWAP_PAWNS:
                return Effects.swapPawns;
            case PUSH_PAWN:
                return Effects.pushPawn;
            case FORBID_MOVE_UP:
                return Effects.forbidMoveUp;
            case FORBID_CURRENT_COORDINATE:
                return Effects.forbidCurrentCoordinate;
            case FORBID_TARGET_COORDINATE:
                return Effects.forbidTargetCoordinate;
        }
        System.err.println(id);
        throw new IllegalStateException();
    }

    @Override
    public Effect deserialize(JsonElement jsonElement, Type type,
                              JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        EffectId id = jsonDeserializationContext.deserialize(jsonElement, EffectId.class);
        return getEffectFromId(id);
    }
}
