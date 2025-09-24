package org.sid;


import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.sid.components.SpriteRenderer;
import org.sid.components.SpriteSheet;
import org.sid.utils.AssetPool;

public class LevelEditorScene extends Scene {


    GameObject obj1;
    GameObject obj2;

    SpriteSheet sprites;

    public LevelEditorScene() {
        System.out.println("Inside level editor scene");

    }

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f(-250, 0));
        if (isLevelLLoaded) {
            return;
        }

        sprites = AssetPool.getSpriteSheet("assets/images/spritesheet.png");

        Transform transform = new Transform();
        transform.setScale(new Vector2f(256, 256));
        transform.setPosition(new Vector2f(200, 100));
        obj1 = new GameObject("Obj1", transform, 2);

        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setColor(new Vector4f(1f, 0, 0, 0.5f));


        obj1.addComponent(renderer);

        this.addGameObjectToScene(obj1);
        this.activeGameObject = obj1;

        Transform transform1 = new Transform();
        transform1.setPosition(new Vector2f(400, 100));
        transform1.setScale(new Vector2f(256, 256));

        SpriteRenderer spriteRenderer = new SpriteRenderer();
        spriteRenderer.setColor(new Vector4f(0, 1f, 0, 0.5f));

        obj2 = new GameObject("Obj2", transform, 1);
        obj2.addComponent(spriteRenderer);
        this.addGameObjectToScene(obj2);


    }

    @Override
    public void update(float dt) {
//        System.out.println("FPS:" + (1.0f / dt));


        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }
        this.renderer.render();
    }

    @Override
    public void imgui() {
        ImGui.begin("test");
        ImGui.text("Test Window");
        ImGui.end();
    }

    private void loadResources() {
        AssetPool.getShaders("assets/shaders/default.glsl");

        SpriteSheet spriteSheet = new SpriteSheet();
        spriteSheet.init(AssetPool.getTextures("assets/images/spritesheet.png"), 16, 16, 26, 0);

        AssetPool.addSpriteSheet("assets/images/spritesheet.png", spriteSheet);
    }


}

