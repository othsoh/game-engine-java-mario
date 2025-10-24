package org.sid.scenes;


import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.sid.components.*;
import org.sid.jade.*;
import org.sid.physics2d.Physics2D;
import org.sid.physics2d.rigidbody.Rigidbody2D;
import org.sid.renderer.DebugDraw;
import org.sid.utils.AssetPool;

public class LevelEditorScene extends Scene {
    private SpriteSheet sprites;
    GameObject leveLEditor = new GameObject("levelEditor",new Transform(new Vector2f()),0);

    Physics2D physics = new Physics2D(1.0f / 60.0f, new Vector2f(0,-10));

    Transform obj1 ;
    Transform obj2;
    Rigidbody2D rigidbody1;
    Rigidbody2D rigidbody2;


    public LevelEditorScene() {

    }

    @Override
    public void init() {

        obj1 = new Transform(new Vector2f(100, 500 ));
        obj2 = new Transform(new Vector2f(200, 500 ));
        rigidbody1 = new Rigidbody2D();
        rigidbody2 = new Rigidbody2D();

        rigidbody1.setRawTransform(obj1);
        rigidbody2.setRawTransform(obj2);

        rigidbody1.setMass(100);
        rigidbody2.setMass(2);

        physics.addRigidbody(rigidbody1);
        physics.addRigidbody(rigidbody2);

        leveLEditor.addComponent(new MouseControls());
        leveLEditor.addComponent(new GridLines());
        loadResources();
        this.camera = new Camera(new Vector2f(-250, 0));
        sprites = AssetPool.getSpriteSheet("assets/images/spritesheets/decorationsAndBlocks.png");
        if (isLevelLoaded) {
            if (!gameObjects.isEmpty()){
                this.activeGameObject = gameObjects.get(0);

            }
            return;
        }

    }

    private void loadResources() {
//        AssetPool.getShader("assets/shaders/default.glsl");
//        AssetPool.getTexture("assets/images/blendImage2.png");

        AssetPool.addSpriteSheet("assets/images/spritesheets/decorationsAndBlocks.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/spritesheets/decorationsAndBlocks.png"),
                        16, 16, 81, 0));

        for (GameObject go : gameObjects){
            if (go.getComponent(SpriteRenderer.class)!= null){
                SpriteRenderer spriteRenderer = go.getComponent(SpriteRenderer.class);
                if (spriteRenderer.getTexture()!= null){
                    spriteRenderer.setTexture(AssetPool.getTexture(spriteRenderer.getTexture().getFilepath()));
                }
            }
        }
    }

    @Override
    public void update(float dt) {


        leveLEditor.update(dt);

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }
//
//        DebugDraw.addBox2D(rigidbody1.getPosition(), new Vector2f(32, 32), 0.0f, new Vector3f(1,0,0), 1);
//        DebugDraw.addBox2D(rigidbody2.getPosition(), new Vector2f(32, 32), 0.0f, new Vector3f(0,1,0), 1);

        physics.update(dt);

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

