package org.sid.components;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.sid.Component;

public class SpriteRenderer extends Component {

    private Vector4f color;

    public SpriteRenderer(){
        init(new Vector4f());
    }

    public SpriteRenderer(Vector4f color){
        init(color);
    }
    private void  init(Vector4f color){
        this.color = color;
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
}
