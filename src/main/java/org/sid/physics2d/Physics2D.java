package org.sid.physics2d;

import org.joml.Vector2f;
import org.sid.physics2d.forces.ForceRegistration;
import org.sid.physics2d.forces.ForceRegistry;
import org.sid.physics2d.forces.Gravity2D;
import org.sid.physics2d.rigidbody.Rigidbody2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Physics2D {

    private ForceRegistry forceRegistry;
    private List<Rigidbody2D> rigidbodies;

    private Gravity2D gravity;

    float fixedUpdate;

    public Physics2D(float fixedUpdate, Vector2f gravity){
        forceRegistry = new ForceRegistry();
        rigidbodies = new ArrayList<>();
        this.gravity= new Gravity2D(gravity);

        this.fixedUpdate = fixedUpdate;

    }

    public void update(float dt){
        // TODO: implement incremental for lagging
        fixedUpdate();
    }

    public void fixedUpdate(){

        forceRegistry.updateForce(fixedUpdate);

        //update the velocities of rigidbodies
        for (Rigidbody2D rigidbody: rigidbodies){
            rigidbody.physicsUpdate(fixedUpdate);
        }
    }

    public void addRigidbody(Rigidbody2D rigidbody){
        this.rigidbodies.add(rigidbody);

        //register gravity
        this.forceRegistry.add(rigidbody, gravity);

    }

    public void removeRigidbody(Rigidbody2D rigidbody2D){
        this.rigidbodies.remove(rigidbody2D);
    }



}
