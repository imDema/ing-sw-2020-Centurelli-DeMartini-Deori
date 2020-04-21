package it.polimi.ingsw.model.serialization;

import com.google.gson.*;
import it.polimi.ingsw.model.action.Effects;
import it.polimi.ingsw.model.action.Effect;

import java.lang.reflect.Type;

class EffectDeserializer implements JsonDeserializer<Effect> {

    enum EffectId {
        MOVE,
        BUILD_BLOCK,
        BUILD_DOME,
        SWAP_PAWNS,
        PUSH_PAWN,
        FORBID_MOVE_UP,
        FORBID_CURRENT_POSITION,
        FORBID_COORDINATE,
        FORBID_OTHER_COORDINATES,
        WIN_ON_JUMP_DOWN
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
            case FORBID_CURRENT_POSITION:
                return Effects.forbidCurrentPosition;
            case FORBID_COORDINATE:
                return Effects.forbidCoordinate;
            case FORBID_OTHER_COORDINATES:
                return Effects.forbidOtherCoordinates;
            case WIN_ON_JUMP_DOWN:
                return Effects.winOnJumpDown;
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
