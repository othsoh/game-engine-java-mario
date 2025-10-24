package org.sid.physics2d.forces;

import org.sid.physics2d.rigidbody.Rigidbody2D;

public class ForceRegistration {

    public Rigidbody2D rigidbody = null;
    public ForceGenerator forceGenerator = null;

    public ForceRegistration(Rigidbody2D rigidbody, ForceGenerator forceGenerator) {
        this.rigidbody = rigidbody;
        this.forceGenerator = forceGenerator;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null){
            return false;
        }
        if (obj.getClass() != ForceRegistration.class){
            return false;
        }

        ForceRegistration other = (ForceRegistration) obj;
        return other.rigidbody == this.rigidbody && other.forceGenerator == this.forceGenerator;
    }
}
