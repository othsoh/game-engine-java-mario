package org.sid;


import imgui.ImGui;
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
    GameObject obj2;

    SpriteSheet sprites;

    public LevelEditorScene() {
        System.out.println("Inside level editor scene");

    }

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f(-250, 0));

        sprites = AssetPool.getSpriteSheet("assets/images/spritesheet.png");

        obj1 = new GameObject("Obj1",
                new Transform(new Vector2f(200,100),
                        new Vector2f(256,256)),
                2);
        obj1.addComponent(new SpriteRenderer(new Vector4f(1f,0,0,0.5f)));
        this.addGameObjectToScene(obj1);
        this.activeGameObject = obj1;

        obj2 = new GameObject("Obj2",
                new Transform(new Vector2f(400,100), new Vector2f(256,256)),
                1);
        obj2.addComponent(new SpriteRenderer(new Vector4f(0,1f,0,0.5f)));
        this.addGameObjectToScene(obj2);

    }
    @Override
    public void update(float dt) {
        System.out.println("FPS:" + (1.0f / dt));


        for( GameObject go: this.gameObjects){
            go.update(dt);
        }
        this.renderer.render();
    }

    @Override
    public void imgui(){
        ImGui.begin("test");
        ImGui.text("Test Window");
        ImGui.end();
    }

    private void loadResources() {
        AssetPool.getShaders("assets/shaders/default.glsl");

        AssetPool.addSpriteSheet("assets/images/spritesheet.png",
                new SpriteSheet(AssetPool.getTextures("assets/images/spritesheet.png"),
                        16,16,26,0
                        ));
    }



}

