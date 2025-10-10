package org.sid.components;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.sid.renderer.Texture;

public class Sprite {

    private Texture texture;
    private float width, height;
    private Vector2f[] texCoords= new Vector2f[]{
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };

    public Sprite(){

    }

    public void init(Texture texture){
        this.texture = texture;
    }


    public Texture getTexture() {
        return texture;
    }


    public Vector2f[] getTexCoords() {
        return texCoords;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setTexCoords(Vector2f[] coords){
        this.texCoords = coords;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }
    public int getTextId(){
        return texture == null? -1 : texture.getId();
    }
}
