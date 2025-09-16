package org.sid;


import org.joml.Vector2f;
import org.joml.Vector4f;
import org.sid.components.SpriteRenderer;
import org.sid.renderer.Texture;
import org.sid.utils.AssetPool;

public class LevelEditorScene extends Scene {


    public LevelEditorScene() {
        System.out.println("Inside level editor scene");

    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f(-250, 0));

        GameObject obj1 = new GameObject("Obj1",new Transform(new Vector2f(100,100), new Vector2f(256,256)));
        obj1.addComponent(new SpriteRenderer(AssetPool.getTextures("assets/images/testImage.png")));
        this.addGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("Obj2",new Transform(new Vector2f(400,100), new Vector2f(256,256)));
        obj2.addComponent(new SpriteRenderer(AssetPool.getTextures("assets/images/testImage2.png")));
        this.addGameObjectToScene(obj2);
        loadResources();
    }

    private void loadResources() {
        AssetPool.getShaders("assets/shaders/default.glsl");

    }

    @Override
    public void update(float dt) {
        System.out.println("FPS:" + (1.0f / dt));

        for( GameObject go: this.gameObjects){
            go.update(dt);
        }
        this.renderer.render();
    }

}

