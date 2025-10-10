package org.sid.components;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.sid.jade.GameObject;
import org.sid.jade.MouseListener;
import org.sid.jade.Window;
import org.sid.scenes.Scene;
import org.sid.utils.Settings;

public class MouseControls extends  Component{

    private GameObject holdingObject = null;

    public void pickupObject(GameObject gameObject){
        this.holdingObject = gameObject;
        Window.getScene().addGameObjectToScene(gameObject);
    }

    public void placeObject(){
        this.holdingObject = null;
    }

    @Override
    public void update(float dt){
        if(this.holdingObject != null){
            holdingObject.transform.position.x = (int) (MouseListener.getOrthoX() / Settings.GRID_WIDTH) * Settings.GRID_WIDTH ;
            holdingObject.transform.position.y = (int) (MouseListener.getOrthoY() / Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT;

            if(MouseListener.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)){
                placeObject();
            }
        }
    }
}
