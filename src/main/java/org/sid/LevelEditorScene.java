package org.sid;


import org.joml.Vector2f;
import org.joml.Vector4f;
import org.sid.components.SpriteRenderer;
import org.sid.utils.AssetPool;

public class LevelEditorScene extends Scene {


    public LevelEditorScene() {
        System.out.println("Inside level editor scene");

    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f(-250, 0));

        int xOffset = 10;
        int yOffset = 10;

        float totalWidth = (float)(600 - xOffset * 2);
        float totalHeight = (float)(300 - yOffset * 2);
        float sizeY = totalHeight / 100.0f;
        float sizeX = totalWidth / 100.0f;

        for (int x=0; x<100;x++){
            for (int y=0; y<100; y++){
                float xPos = xOffset + (x * sizeX);
                float yPos = yOffset + (y * sizeY);

                GameObject go = new GameObject("Obj", new Transform(new Vector2f(xPos, yPos), new Vector2f(sizeX, sizeY)));
                go.addComponent(new SpriteRenderer(new Vector4f(xPos/totalWidth, yPos/ totalHeight, 1,1)));
                this.addGameObjectToScene(go);
            }
        }
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

