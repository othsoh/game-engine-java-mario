package org.sid.scenes;


import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.sid.components.*;
import org.sid.jade.*;
import org.sid.renderer.DebugDraw;
import org.sid.utils.AssetPool;

public class LevelEditorScene extends Scene {
    private GameObject obj1;
    private SpriteSheet sprites;
    SpriteRenderer obj1Sprite;
    GameObject leveLEditor = new GameObject("levelEditor",new Transform(new Vector2f()),0);

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        leveLEditor.addComponent(new MouseControls());
        leveLEditor.addComponent(new GridLines());
        loadResources();
        this.camera = new Camera(new Vector2f(-250, 0));
        sprites = AssetPool.getSpriteSheet("assets/images/spritesheets/decorationsAndBlocks.png");
//        DebugDraw.addLine2D(new Vector2f(0,0),new Vector2f(200,200),new Vector3f(1,0,0),90);
//        DebugDraw.addLine2D(new Vector2f(175,175),new Vector2f(225,175),new Vector3f(1,0,0),120);
//        DebugDraw.addLine2D(new Vector2f(225,175),new Vector2f(225,225),new Vector3f(1,0,0),120);
//        DebugDraw.addLine2D(new Vector2f(225,225),new Vector2f(175,225),new Vector3f(1,0,0),120);
//        DebugDraw.addLine2D(new Vector2f(175,225),new Vector2f(175,175),new Vector3f(1,0,0),120);

        if (isLevelLoaded) {
//            this.activeGameObject = gameObjects.get(0);
//            this.activeGameObject.addComponent(new Rigidbody());
            return;
        }

//
//
//        obj1 = new GameObject("Object 1", new Transform(new Vector2f(200, 100),
//                new Vector2f(256, 256)), 2);
//        obj1Sprite = new SpriteRenderer();
//        obj1Sprite.setColor(new Vector4f(1, 0, 0, 1));
//        obj1.addComponent(obj1Sprite);
//        obj1.addComponent(new Rigidbody());
//
//        this.addGameObjectToScene(obj1);
//        this.activeGameObject = obj1;
//
//        GameObject obj2 = new GameObject("Object 2",
//                new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), 1);
//        SpriteRenderer obj2SpriteRenderer = new SpriteRenderer();
//        Sprite obj2Sprite = new Sprite();
//        obj2Sprite.setTexture(AssetPool.getTexture("assets/images/blendImage2.png"));
//        obj2SpriteRenderer.setSprite(obj2Sprite);
//        obj2.addComponent(obj2SpriteRenderer);
//        this.addGameObjectToScene(obj2);
    }

    private void loadResources() {
//        AssetPool.getShader("assets/shaders/default.glsl");
//        AssetPool.getTexture("assets/images/blendImage2.png");

        // TODO: FIX TEXTURE SAVE SYSTEM TO USE PATH INSTEAD OF ID
        AssetPool.addSpriteSheet("assets/images/spritesheets/decorationsAndBlocks.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/spritesheets/decorationsAndBlocks.png"),
                        16, 16, 81, 0));
    }

    @Override
    public void update(float dt) {

        leveLEditor.update(dt);

        DebugDraw.addBox2D(new Vector2f(200,200),new Vector2f(400, 200), 60, new Vector3f(1,0,0), 1);
        DebugDraw.addCircle2D(new Vector2f(200,200),50,new Vector3f(1,0,0),1);

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }

    @Override
    public void imgui() {
        ImGui.begin("Blocks and Decoration");
//        ImGui.text("Some random text");

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);

        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);

        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowsX2 = windowSize.x + windowPos.x;

        for (int i=0; i < sprites.size(); i++){
            Sprite sprite = sprites.getSprite(i);


            float spriteWidth = sprite.getWidth()*2;
            float spriteHeight = sprite.getHeight()*2;

            int id = sprite.getTextId();

            Vector2f[] texCoords = sprite.getTexCoords();

            ImGui.pushID(i);
            if(ImGui.imageButton(id,spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x,texCoords[2].y)){
                leveLEditor.getComponent(MouseControls.class).pickupObject(Prefabs.generateGameObject(sprite, spriteWidth, spriteHeight));
            }
            ImGui.popID();

            ImVec2 currentButton = new ImVec2();
            ImGui.getItemRectMax(currentButton);
            float currentButtonX2 =currentButton.x;
            float nextButtonX2 = currentButtonX2 + spriteWidth + itemSpacing.x;
            if( i + 1 < sprites.size() && nextButtonX2 < windowsX2){
                ImGui.sameLine();
            }
        }

        ImGui.end();
    }
}

