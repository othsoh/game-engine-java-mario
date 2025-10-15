package org.sid.physics2d.primitives;

import org.joml.Vector2f;
import org.sid.physics2d.rigidbody.Rigidbody2D;


// Axis aligned bounding box (no rotation)
public class AABB {

    private Vector2f size = new Vector2f();
    private Vector2f halfSize = new Vector2f();
    private Rigidbody2D rigidbody2D = null;

    public AABB(){
        this.halfSize = new Vector2f(size).div(2);
    }

    public AABB(Vector2f min, Vector2f max){
//        this.rigidbody2D = new Rigidbody2D();
        this.size = new Vector2f(max).sub(min);
        this.halfSize = new Vector2f(size).div(2);

    }

    public Vector2f getMin(){
        return new Vector2f(rigidbody2D.getPosition()).sub(halfSize);
    }

    public Vector2f getMax(){
        return new Vector2f(rigidbody2D.getPosition()).add(halfSize);
    }
}
