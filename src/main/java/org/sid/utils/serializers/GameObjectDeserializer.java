package org.sid.utils.serializers;

import com.google.gson.*;
import org.sid.components.Component;
import org.sid.jade.GameObject;
import org.sid.jade.Transform;

import java.lang.reflect.Type;

public class GameObjectDeserializer implements JsonDeserializer<GameObject> {
    @Override
    public GameObject deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject json = jsonElement.getAsJsonObject();
        String name = json.get("name").getAsString();
        JsonArray components = json.getAsJsonArray("components");
        Transform transform = jsonDeserializationContext.deserialize(json.get("transform"), Transform.class);
        int zIndex = jsonDeserializationContext.deserialize(json.get("zIndex"), int.class);

        GameObject go = new GameObject(name, transform, zIndex);

        for (JsonElement c : components){
            Component component = jsonDeserializationContext.deserialize(c, Component.class);
            go.addComponent(component);
        }
        return go;
    }
}
