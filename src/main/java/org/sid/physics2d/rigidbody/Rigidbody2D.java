package org.sid.physics2d.rigidbody;

import org.joml.Vector2f;
import org.sid.components.Component;

public class Rigidbody2D extends Component {

    private float rotation= 0.0f;
    private Vector2f position = new Vector2f();

    private Vector2f linearVelocity = new Vector2f();
    private float angularVelocity = 0.0f;
    private float angularDamping = 0.0f;
    private float linearDamping = 0.0f;

    private boolean fixedRotation = false;


    public Rigidbody2D(){

    }

    public Rigidbody2D(Vector2f position, float rotation){
        this.position= position;
        this.rotation = rotation;
    }

    public Vector2f getPosition() {
        return position;
    }

    public float getRotation() {
        return rotation;
    }

    public void setTransform(Vector2f position, float rotation){
        this.position.set(position);
        this.rotation=rotation;
    }
    public void setTransform(Vector2f position){
        this.position.set(position);
    }
}
