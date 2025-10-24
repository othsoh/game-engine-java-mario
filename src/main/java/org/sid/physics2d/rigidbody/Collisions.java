package org.sid.physics2d.rigidbody;

import org.joml.Vector2f;
import org.sid.physics2d.primitives.Circle;

public class Collisions {

    public static CollisionManifolds findCollisionFeatures(Circle a, Circle b){
        CollisionManifolds result = new CollisionManifolds();

        float sumRadii = a.getRadius() + b.getRadius();
        Vector2f distanceBetweenCenters = new Vector2f(b.getCenter()).sub(a.getCenter());

        if (distanceBetweenCenters.lengthSquared() > sumRadii) return result;

        float depth = (sumRadii - distanceBetweenCenters.lengthSquared()) * 0.5f;
        Vector2f normal = new Vector2f(distanceBetweenCenters);
        normal.normalize();

        float centerToPoint = a.getRadius() -depth;

        Vector2f contactPoint = new Vector2f(a.getCenter()).add(
                new Vector2f(centerToPoint).mul(normal)
        );

        result = new CollisionManifolds(normal, depth);
        result.addContactPoint(contactPoint);

        return result;
    }
}
