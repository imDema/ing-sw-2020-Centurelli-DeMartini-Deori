package it.polimi.ingsw.model.serialization;

import com.google.gson.*;

import java.lang.reflect.Type;

class InterfaceSerializer<T> implements JsonSerializer<T>, JsonDeserializer<T>{
    private static final String CLASS = "Class";
    private static final String CONTENT = "Content";

    @Override
    public JsonElement serialize(Object o, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(CLASS, o.getClass().getName());
        jsonObject.add(CONTENT, jsonSerializationContext.serialize(o));
        return jsonObject;
    }

    @Override
    public T deserialize(JsonElement jsonElement, Type type,
                         JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String className = jsonObject.getAsJsonPrimitive(CLASS).getAsString();
        try {
            return jsonDeserializationContext.deserialize(jsonObject.get(CONTENT), Class.forName(className));
        } catch(ClassNotFoundException e) {
            throw new JsonParseException(e);
        }
    }
}
