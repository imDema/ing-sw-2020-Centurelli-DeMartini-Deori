package it.polimi.ingsw.serialization;

import com.google.gson.*;
import it.polimi.ingsw.view.messages.AddUserMessage;
import it.polimi.ingsw.view.messages.ChooseGodMessage;
import it.polimi.ingsw.view.messages.Message;
import it.polimi.ingsw.view.messages.MessageId;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.AbstractMap.SimpleImmutableEntry;

class MessageSerializer implements JsonSerializer<Message>, JsonDeserializer<Message> {
    private final String TYPE = "type";
    private final String CONTENT = "content";

    private final Map<MessageId, Type> map = Map.ofEntries(
            new SimpleImmutableEntry<>(MessageId.ADD_USER, AddUserMessage.class),
            new SimpleImmutableEntry<>(MessageId.CHOOSE_GOD, ChooseGodMessage.class)
    );

    @Override
    public Message deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        // Deserialize reversing the mapping done in Message.serialize
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        MessageId msgType = jsonDeserializationContext.deserialize(jsonObject.get(TYPE), MessageId.class);
        Type actualType = map.get(msgType);

        if (actualType != null) {
            return jsonDeserializationContext.deserialize(jsonObject.get(CONTENT), actualType);
        } else {
            throw new JsonParseException("No mapping for MessageType: " + msgType + " as Message class");
        }
    }

    @Override
    public JsonElement serialize(Message message, Type type, JsonSerializationContext jsonSerializationContext) {
        // Use Message.serialize to get class specific serialization
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(TYPE, jsonSerializationContext.serialize(message.getSerializationId()));
        jsonObject.add(CONTENT, jsonSerializationContext.serialize(message));
        return jsonObject;
    }
}
