package org.sid.components;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.sid.renderer.Texture;

public class Sprite {

    private Texture texture;
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

//    public Sprite(Texture texture) {
//        this.texture = texture;
//        Vector2f[] textCoords = new Vector2f[]{
//                new Vector2f(1, 1),
//                new Vector2f(1, 0),
//                new Vector2f(0, 0),
//                new Vector2f(0, 1)
//        };
//        this.texCoords = textCoords;
//    }
//    public Sprite(Texture texture, Vector2f[] texCoords){
//        this.texture = texture;
//        this.texCoords = texCoords;
//    }

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
}
