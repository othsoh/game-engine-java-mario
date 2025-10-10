package org.sid.utils.serializers;

import com.google.gson.*;
import org.sid.components.Component;

import java.lang.reflect.Type;


public class ComponentSerializer implements JsonSerializer<Component>, JsonDeserializer<Component> {
    @Override
    public Component deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject json = jsonElement.getAsJsonObject();
        String componentType = json.get("type").getAsString();
        JsonElement properties = json.get("properties");

        try{
            return jsonDeserializationContext.deserialize(properties, Class.forName(componentType));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unkown element for type: " + type,e);
        }
    }

    @Override
    public JsonElement serialize(Component component, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject json = new JsonObject();
        json.add("type", new JsonPrimitive(component.getClass().getCanonicalName()));
        json.add("properties", jsonSerializationContext.serialize(component, component.getClass()));
        return json;
    }
}