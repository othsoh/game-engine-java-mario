package org.sid.physics2d.primitives;

import org.joml.Vector2f;
import org.sid.physics2d.rigidbody.Rigidbody2D;
import org.sid.utils.JMath;

public class Box2D {

    private Vector2f size = new Vector2f();
    private Vector2f halfSize;
    private Rigidbody2D rigidbody2D = null;

    public Box2D(){
        this.halfSize = new Vector2f(size).div(2);
    }

    public Box2D(Vector2f min, Vector2f max){
//        this.rigidbody2D = new Rigidbody2D();
        this.size = new Vector2f(max).sub(min);
        this.halfSize = new Vector2f(size).div(2);

    }

    public Vector2f getLocalMin(){
        return new Vector2f(rigidbody2D.getPosition()).sub(halfSize);
    }

    public Vector2f getLocalMax(){
        return new Vector2f(rigidbody2D.getPosition()).add(halfSize);
    }

    public Vector2f[] getVertices(){
        Vector2f[] vertices = {
                new Vector2f(getLocalMin()), new Vector2f(getLocalMax().x, getLocalMin().y),
                new Vector2f(getLocalMax()), new Vector2f(getLocalMin().x, getLocalMax().y)
        };

        if (rigidbody2D.getRotation() != 0.0f){
            for (Vector2f vertex : vertices){
                JMath.rotate(vertex, rigidbody2D.getRotation(), rigidbody2D.getPosition());
            }
        }

        return vertices;
    }

    public float getRotationDeg (){
        return this.rigidbody2D.getRotation();
    }
    public Vector2f getCenter(){
        return this.rigidbody2D.getPosition();
    }

    public Vector2f getHalfSize(){
        return this.halfSize;
    }
}
