package org.sid.components;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.sid.Component;
import org.sid.renderer.Texture;

public class SpriteRenderer extends Component {

    private Vector4f color;

    private Texture texture;

    private Vector2f[] textureCoords;

    public SpriteRenderer(){
        init(new Vector4f());
    }

    public SpriteRenderer(Vector4f color){
        init(color);
        this.texture = null;
    }
    public SpriteRenderer(Texture texture){
        init(texture);
        this.color = new Vector4f(1,1,1,1);
    }
    private void  init(Vector4f color){
        this.color = color;
    }
    private void  init(Texture texture){
        this.texture = texture;
    }
    @Override
    public void start() {
    }

    @Override
    public void update(float dt) {

    }

    public Vector4f getColor() {
        return this.color;
    }

    public Vector2f[] getTextureCoords() {
        Vector2f[] textCoords = new Vector2f[]{
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
        };
        return textCoords;
    }

    public Texture getTexture() {
        return this.texture;
    }
}
