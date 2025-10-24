package org.sid.physics2d.rigidbody;

import org.joml.Vector2f;
import org.sid.components.Component;
import org.sid.jade.Transform;

public class Rigidbody2D extends Component {

    private Transform rawTransform;

    private float rotation= 0.0f;
    private Vector2f position = new Vector2f();

    private float mass= 0.0f;
    private float inverseMass= 0.0f;

    private Vector2f forceAccum = new Vector2f();
    private Vector2f linearVelocity = new Vector2f();
    private float angularVelocity = 0.0f;
    private float angularDamping = 0.0f;
    private float linearDamping = 0.0f;

    private boolean fixedRotation = false;

    public Rigidbody2D(){

    }

    public void physicsUpdate(float dt){
        if (this.mass == 0.0f) return;
        // a = Sum(forces) * 1/m
        Vector2f acceleration = new Vector2f(forceAccum).mul(inverseMass);

        // v += a * dt
        this.linearVelocity.add(acceleration.mul(dt));

        // p += v * dt
        this.position.add(new Vector2f(linearVelocity).mul(dt));

        syncCollisionTransform();
        clearAccum();
    }

    private void syncCollisionTransform() {
        if (this.rawTransform !=null){
            this.rawTransform.position.set(this.position);
        }
    }

    private void clearAccum() {
        this.forceAccum.zero();
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

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
        if (this.mass != 0.0f){
            this.inverseMass = 1.0f / mass;
        }
    }

    public void setRawTransform(Transform rawTransform) {
        this.rawTransform = rawTransform;
        this.position.set(rawTransform.position);
    }

    public void addForce(Vector2f force){
        this.forceAccum.add(force);
    }
}
