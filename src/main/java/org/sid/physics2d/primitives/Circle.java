package org.sid.physics2d.primitives;

import org.joml.Vector2f;
import org.sid.physics2d.rigidbody.Rigidbody2D;

public class Circle {

    private float radius = 0.0f;
    private Rigidbody2D rigidbody = null;

    public Circle(float radius){
        this.radius= radius;
    }

    public float getRadius() {
        return radius;
    }

    public Rigidbody2D getRigidbody() {
        return rigidbody;
    }

    public Vector2f getCenter(){
        return this.rigidbody.getPosition();
    }
}
