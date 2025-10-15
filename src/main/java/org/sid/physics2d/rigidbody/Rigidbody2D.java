package org.sid.physics2d.rigidbody;

import org.joml.Vector2f;
import org.sid.components.Component;

public class Rigidbody2D extends Component {

    private float rotation= 0.0f;
    private Vector2f position = new Vector2f();

    public Rigidbody2D(){

    }

    public Rigidbody2D(Vector2f position, float rotation){
        this.position= position;
        this.rotation = rotation;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}
