package org.sid.physics2d.forces;

import org.sid.physics2d.rigidbody.Rigidbody2D;

public interface ForceGenerator {

    void updateForce(Rigidbody2D rigidbody, float dt);
}
