package org.sid;

import imgui.ImGui;
import org.sid.renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    protected List<GameObject> gameObjects = new ArrayList<>();
    private boolean isRunning = false;
    protected GameObject activeGameObject = null;

    public Scene() {

    }

    public void start() {
        for (GameObject gameObject : gameObjects) {
            gameObject.start();
            this.renderer.add(gameObject);
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject gameObject) {
        if(!isRunning){
            gameObjects.add(gameObject);
        }else {
            gameObjects.add(gameObject);
            gameObject.start();
            this.renderer.add(gameObject);
        }
    }


    public void init() {

    }


    public abstract void update(float dt);

    public Camera camera(){
        return this.camera;
    }

    public void sceneImgui(){
        if(activeGameObject != null){
            ImGui.begin("Inspector");
            activeGameObject.imgui();
            ImGui.end();
        }
        imgui();
    }

    public void imgui(){

    }
}
