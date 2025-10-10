package org.sid.scenes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;
import org.sid.components.Component;
import org.sid.jade.Camera;
import org.sid.jade.GameObject;
import org.sid.renderer.Renderer;
import org.sid.utils.serializers.ComponentSerializer;
import org.sid.utils.serializers.GameObjectDeserializer;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    protected List<GameObject> gameObjects = new ArrayList<>();
    private boolean isRunning = false;
    protected GameObject activeGameObject = null;
    protected boolean isLevelLoaded= false;


    public int gameObjectSize = 0;

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
            gameObjectSize++;
        }else {
            gameObjects.add(gameObject);
            gameObjectSize++;
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

    public void saveAndExit(){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class,new ComponentSerializer())
                .registerTypeAdapter(GameObject.class,new GameObjectDeserializer())
                .create();

        try {
            FileWriter file = new FileWriter("level.txt", false);
            System.out.println(gson.toJson(this.gameObjects));
            file.write(gson.toJson(this.gameObjects));
            file.close();
            System.out.println("GameObjects count: "+ gameObjects.size());

        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public void load(){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class,new ComponentSerializer())
                .registerTypeAdapter(GameObject.class,new GameObjectDeserializer())
                .create();
        String inFile = "";

        try {
            inFile = new String(Files.readAllBytes(Paths.get("level.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inFile.isEmpty()){

            int maxCompID = -1;
            int maxGoID = -1;

            GameObject[] gameObjects = gson.fromJson(inFile, GameObject[].class);
            for (GameObject obj : gameObjects ){
                addGameObjectToScene(obj);
                for (Component c : obj.getAllComponents()){
                    if (c.getUid() >maxCompID){
                        maxCompID = c.getUid();
                    }
                }
                if(obj.getUid()> maxGoID){
                    maxGoID= obj.getUid();
                }
            }
            maxGoID++;
            maxCompID++;

            GameObject.init(maxGoID);
            Component.init(maxCompID);
            this.isLevelLoaded = true;
        }

    }


}
