package org.sid.physics2d.primitives;

import org.joml.Vector2f;
import org.sid.physics2d.rigidbody.Rigidbody2D;


// Axis aligned bounding box (no rotation)
public class AABB {

    private Vector2f size = new Vector2f();
    private Vector2f halfSize = new Vector2f();
    private Rigidbody2D rigidbody = null;

    public AABB(){
        this.halfSize = new Vector2f(size).div(2);
    }

    public AABB(Vector2f min, Vector2f max){
//        this.rigidbody = new Rigidbody2D();
        this.size = new Vector2f(max).sub(min);
        this.halfSize = new Vector2f(size).div(2);

    }

    public Vector2f getMin(){
        return new Vector2f(rigidbody.getPosition()).sub(halfSize);
    }

    public Vector2f getMax(){
        return new Vector2f(rigidbody.getPosition()).add(halfSize);
    }

    public void setRigidbody2D(Rigidbody2D rigidbody) {
        this.rigidbody = rigidbody;
    }

    public void setSize(Vector2f size) {
        this.size.set(size);
        this.halfSize.set(new Vector2f(size).div(2.0f));
    }
}
