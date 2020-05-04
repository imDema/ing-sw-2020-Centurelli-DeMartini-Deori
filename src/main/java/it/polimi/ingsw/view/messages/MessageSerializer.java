package it.polimi.ingsw.view.messages;

import com.google.gson.*;

import java.lang.reflect.Type;

public class MessageSerializer implements JsonSerializer<Message>, JsonDeserializer<Message> {
    @Override
    public Message deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        // Deserialize reversing the mapping done in Message.serialize

        return null;
    }

    @Override
    public JsonElement serialize(Message message, Type type, JsonSerializationContext jsonSerializationContext) {
        // Use Message.serialize to get class specific serialization

        return null;
    }
}
