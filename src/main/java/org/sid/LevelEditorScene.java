package org.sid;


import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.system.CallbackI;
import org.sid.components.Sprite;
import org.sid.components.SpriteRenderer;
import org.sid.components.SpriteSheet;
import org.sid.renderer.Texture;
import org.sid.utils.AssetPool;

public class LevelEditorScene extends Scene {


    GameObject obj1;

    public LevelEditorScene() {
        System.out.println("Inside level editor scene");

    }

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f(-250, 0));

        SpriteSheet sprites = AssetPool.getSpriteSheet("assets/images/spritesheet.png");

        obj1 = new GameObject("Obj1",new Transform(new Vector2f(100,100), new Vector2f(256,256)));
        obj1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        this.addGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("Obj2",new Transform(new Vector2f(400,100), new Vector2f(256,256)));
        obj2.addComponent(new SpriteRenderer(sprites.getSprite(25)));
        this.addGameObjectToScene(obj2);

    }

    private void loadResources() {
        AssetPool.getShaders("assets/shaders/default.glsl");

        AssetPool.addSpriteSheet("assets/images/spritesheet.png",
                new SpriteSheet(AssetPool.getTextures("assets/images/spritesheet.png"),
                        16,16,26,0
                        ));
    }

    @Override
    public void update(float dt) {
        System.out.println("FPS:" + (1.0f / dt));

        this.obj1.transform.position.x += 10 * dt;

        for( GameObject go: this.gameObjects){
            go.update(dt);
        }
        this.renderer.render();
    }

}

