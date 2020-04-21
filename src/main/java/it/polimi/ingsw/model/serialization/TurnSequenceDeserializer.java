package it.polimi.ingsw.model.serialization;

import com.google.gson.*;
import it.polimi.ingsw.model.player.turnsequence.BranchingTurnSequence;
import it.polimi.ingsw.model.player.turnsequence.LinearTurnSequence;
import it.polimi.ingsw.model.player.turnsequence.TurnSequence;

import java.lang.reflect.Type;

class TurnSequenceDeserializer implements JsonDeserializer<TurnSequence>{
    private static final String TYPE = "type";
    private static final String CONTENT = "content";

    enum TurnSequenceId {
        LINEAR,
        BRANCH_ON_SECOND,
    }

    @Override
    public TurnSequence deserialize(JsonElement jsonElement, Type type,
                         JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        TurnSequenceId id = jsonDeserializationContext.deserialize(jsonObject.get(TYPE), TurnSequenceId.class);
        switch (id) {
            case LINEAR:
                return jsonDeserializationContext.deserialize(jsonObject.get(CONTENT), LinearTurnSequence.class);
            case BRANCH_ON_SECOND:
                return jsonDeserializationContext.deserialize(jsonObject.get(CONTENT), BranchingTurnSequence.class);

            default:
                throw new JsonParseException(new IllegalStateException());
        }
    }
}
