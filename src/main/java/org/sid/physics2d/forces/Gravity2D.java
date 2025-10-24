package org.sid.physics2d.forces;

import org.joml.Vector2f;
import org.sid.physics2d.rigidbody.Rigidbody2D;

public class Gravity2D implements ForceGenerator {

    //acceleration
    private Vector2f gravity;

    public Gravity2D(Vector2f gravity){
        this.gravity = new Vector2f(gravity);
    }

    @Override
    public void updateForce(Rigidbody2D rigidbody, float dt) {
        //f = m * a
        rigidbody.addForce(new Vector2f(gravity).mul(rigidbody.getMass()));

    }
}
