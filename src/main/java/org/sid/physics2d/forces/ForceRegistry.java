package org.sid.physics2d.forces;

import org.sid.physics2d.rigidbody.Rigidbody2D;

import java.util.ArrayList;
import java.util.List;

public class ForceRegistry {

    List<ForceRegistration> forceRegistrations;

    public ForceRegistry() {
        this.forceRegistrations = new ArrayList<>();
    }

    public void add(ForceRegistration force){
        forceRegistrations.add(force);
    }

    public void add(Rigidbody2D rigidbody, ForceGenerator forceGenerator){
        ForceRegistration force = new ForceRegistration(rigidbody, forceGenerator);
        forceRegistrations.add(force);
    }

    public void remove(ForceRegistration force){
        forceRegistrations.remove(force);
    }


    public void remove(Rigidbody2D rigidbody, ForceGenerator forceGenerator){
        ForceRegistration force = new ForceRegistration(rigidbody, forceGenerator);
        forceRegistrations.add(force);
    }

    public void clear(){
        forceRegistrations.clear();
    }

    public void updateForce(float dt){
        for (ForceRegistration force : forceRegistrations){
            force.forceGenerator.updateForce(force.rigidbody,dt);
        }
    }

    public void zeroForces(){
        for (ForceRegistration force : forceRegistrations){
            //TODO: Implement ME
//            force.rigidbody.zeroForces();
        }
    }

}
