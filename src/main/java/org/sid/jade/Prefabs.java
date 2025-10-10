package org.sid.jade;

import org.joml.Vector2f;
import org.sid.components.Sprite;
import org.sid.components.SpriteRenderer;

public class Prefabs {

    public static GameObject generateGameObject(Sprite sprite, float sizeX, float sizeY ){
        GameObject block  = new GameObject("Game_Object_Gen",
                new Transform(new Vector2f(), new Vector2f(sizeX,sizeY)), 0);
        SpriteRenderer renderer = new SpriteRenderer();

        renderer.setSprite(sprite);
        block.addComponent(renderer);

        return block;
    }
}
