package it.polimi.ingsw.serialization;

import com.google.gson.*;
import it.polimi.ingsw.view.messages.*;

import java.lang.reflect.Type;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;

class MessageSerializer implements JsonSerializer<Message>, JsonDeserializer<Message> {
    private final String TYPE = "type";
    private final String CONTENT = "content";

    private final Map<MessageId, Type> map = Map.ofEntries(
            new SimpleImmutableEntry<>(MessageId.ACTION_READY, ActionsReadyMessage.class),
            new SimpleImmutableEntry<>(MessageId.ADD_USER, AddUserMessage.class),
            new SimpleImmutableEntry<>(MessageId.BUILD, BuildMessage.class),
            new SimpleImmutableEntry<>(MessageId.CHECK_ACTION, CheckActionMessage.class),
            new SimpleImmutableEntry<>(MessageId.CHOOSE_GOD, ChooseGodMessage.class),
            new SimpleImmutableEntry<>(MessageId.CHOOSE_FIRST_PLAYER, ChooseFirstPlayerMessage.class),
            new SimpleImmutableEntry<>(MessageId.ELIMINATION, EliminationMessage.class),
            new SimpleImmutableEntry<>(MessageId.EXECUTE_ACTION, ExecuteActionMessage.class),
            new SimpleImmutableEntry<>(MessageId.GOD_CHOSEN, GodChosenMessage.class),
            new SimpleImmutableEntry<>(MessageId.GODS_AVAILABLE, GodsAvailableMessage.class),
            new SimpleImmutableEntry<>(MessageId.MOVE, MoveMessage.class),
            new SimpleImmutableEntry<>(MessageId.PAWN_PLACED, PawnPlacedMessage.class),
            new SimpleImmutableEntry<>(MessageId.PING, PingMessage.class),
            new SimpleImmutableEntry<>(MessageId.PLACE_PAWNS, PlacePawnsMessage.class),
            new SimpleImmutableEntry<>(MessageId.REQUEST_PLACE_PAWNS, RequestPlacePawnsMessage.class),
            new SimpleImmutableEntry<>(MessageId.RESULT, ResultMessage.class),
            new SimpleImmutableEntry<>(MessageId.SELECT_GODS, SelectGodsMessage.class),
            new SimpleImmutableEntry<>(MessageId.SELECT_PLAYER_NUMBER, SelectPlayerNumberMessage.class),
            new SimpleImmutableEntry<>(MessageId.SERVER_ERROR, ServerErrorMessage.class),
            new SimpleImmutableEntry<>(MessageId.SIZE_SELECTED, SizeSelectedMessage.class),
            new SimpleImmutableEntry<>(MessageId.TURN_CHANGE, TurnChangeMessage.class),
            new SimpleImmutableEntry<>(MessageId.USER_JOINED, UserJoinedMessage.class),
            new SimpleImmutableEntry<>(MessageId.WIN, WinMessage.class)
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
