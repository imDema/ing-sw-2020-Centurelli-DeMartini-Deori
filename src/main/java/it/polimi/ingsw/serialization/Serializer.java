package it.polimi.ingsw.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.action.Check;
import it.polimi.ingsw.model.action.Effect;
import it.polimi.ingsw.model.player.God;
import it.polimi.ingsw.model.player.turnsequence.TurnSequence;
import it.polimi.ingsw.view.messages.Message;

import java.lang.reflect.Type;
import java.util.List;

public abstract class Serializer {
    private static Gson gson = null;

    public static String serializeMessage (Message message) {
        return getGson().toJson(message, Message.class);
    }

    public static God deserializeGod(String s) {
        return getGson().fromJson(s, God.class);
    }

    public static List<God> deserializeGodList(String s) {
        Type godListType = new TypeToken<List<God>>() {}.getType();
        return getGson().fromJson(s, godListType);
    }

    public static Message deserializeMessage(String s) {
        return getGson().fromJson(s, Message.class);
    }

    // Lazy load
    private static Gson getGson() {
        if (gson == null) {
            gson = buildGson();
        }
        return gson;
    }

    private static Gson buildGson() {
        return new GsonBuilder()
                .registerTypeAdapter(TurnSequence.class, new TurnSequenceDeserializer())
                .registerTypeAdapter(Effect.class, new EffectDeserializer())
                .registerTypeAdapter(Check.class, new CheckDeserializer())
                .registerTypeAdapter(Message.class, new MessageSerializer())
                // .setPrettyPrinting() // For testing
                .create();
    }
}
