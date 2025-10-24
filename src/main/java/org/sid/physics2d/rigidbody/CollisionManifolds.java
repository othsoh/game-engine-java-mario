package org.sid.physics2d.rigidbody;

import org.joml.Vector2f;

import java.util.List;

public class CollisionManifolds {

     private boolean isColliding;
     private Vector2f normal;
     private List<Vector2f> contactPoints;
     private float depth;

     public CollisionManifolds(){
         isColliding= false;
         normal = new Vector2f();
         depth=0.0f;
     }

     public CollisionManifolds(Vector2f normal, float depth){
        isColliding= true;
        this.normal = normal;
        this.depth=depth;
    }

    public void addContactPoint(Vector2f point){
         this.contactPoints.add(point);
    }
}
