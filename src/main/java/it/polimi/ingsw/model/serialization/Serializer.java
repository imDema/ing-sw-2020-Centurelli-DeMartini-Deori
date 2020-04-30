package it.polimi.ingsw.model.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.action.Check;
import it.polimi.ingsw.model.action.Effect;
import it.polimi.ingsw.model.player.God;
import it.polimi.ingsw.model.player.turnsequence.TurnSequence;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Serializer {
    private static Gson gson = null;

    public static String serialize (Object obj) {
        Gson gson = getGson();
        return gson.toJson(obj);
    }

    public static God getGod(String s) {
        Gson gson = getGson();
        return gson.fromJson(s, God.class);
    }

    public static List<God> getGodArray(String s) {
        Gson gson = getGson();
        Type godListType = new TypeToken<List<God>>() {}.getType();

        return gson.fromJson(s, godListType);
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
                // .setPrettyPrinting() // For testing
                .create();
    }
}
