package org.sid.physics2d.rigidbody;

import org.joml.Vector2f;
import org.lwjgl.system.CallbackI;
import org.sid.physics2d.primitives.AABB;
import org.sid.physics2d.primitives.Box2D;
import org.sid.physics2d.primitives.Circle;
import org.sid.renderer.Line2D;
import org.sid.utils.JMath;

import javax.sound.sampled.Line;
import java.util.Vector;

public class IntersectionDetector2D {

    //==================================================
    // POINT VS PRIMITIVES
    //==================================================


    //point vs line
    public static boolean pointOnLine (Line2D line, Vector2f point){
        // y = mx+c
        float dx = line.getTo().x - line.getFrom().x;
        float dy = line.getTo().y - line.getFrom().y;

        if (dx == 0f){
            return JMath.compare(point.x, line.getFrom().x);
        }

        float m = dy / dx;
        float c =  line.getFrom().y - m * line.getFrom().x;

        float y = m * point.x + c;

        return point.y == y;

    }

    //point vs circle
    public static boolean pointInCircle(Circle circle, Vector2f point){
        //Compare radius with distance between center and point
        //distance = sqrt((p.x-c.x)^2 + (p.y-c.y)^2)
        // p= point ; c= center

        float distance  = (float) Math.sqrt(Math.pow((point.x - circle.getCenter().x), 2) + Math.pow((point.y - circle.getCenter().y), 2));
        return distance < circle.getRadius();
    }

    //point vs Axis Aligned Bounding Box
    public static boolean pointInAABB(AABB box, Vector2f point){
        Vector2f min = box.getMin();
        Vector2f max = box.getMax();
        return point.x  <= max.x && min.x <= point.x && point.y <= max.y && min.y <= point.y;
    }


    //point vs Box2D
    public static boolean pointInBox2D(Box2D box, Vector2f point){
        Vector2f min = box.getMin();
        Vector2f max = box.getMax();
        Vector2f rotatedPoint = new Vector2f(point);
        JMath.rotate(rotatedPoint,box.getRotationDeg(),box.getCenter());
        return rotatedPoint.x  <= max.x && min.x <= rotatedPoint.x && rotatedPoint.y <= max.y && min.y <= rotatedPoint.y;
    }


    //line vs circle
    public static boolean lineAndCircle(Line2D line, Circle circle){
        if (pointInCircle(circle, line.getTo()) || pointInCircle(circle, line.getFrom())){
            return true;
        }
        Vector2f ab = new Vector2f(line.getTo()).sub(line.getFrom());

        // Project point (circle position) onto ab (line segment)
        // parameterized position t
        Vector2f circleCenter = circle.getCenter();
        Vector2f centerToLineStart = new Vector2f(circleCenter).sub(line.getFrom());
        float t = centerToLineStart.dot(ab) / ab.dot(ab);

        if (t < 0.0f || t > 1.0f) {
            return false;
        }

        // Find the closest point to the line segment
        Vector2f closestPoint = new Vector2f(line.getFrom()).add(ab.mul(t));

        return pointInCircle(circle, closestPoint);

    }
    //line vs AABB
    public static boolean lineAndAABB(Line2D line, AABB box){

        if(pointInAABB(box, line.getFrom()) || pointInAABB(box, line.getTo())){
            return true;
        }

        Vector2f unitVector = new Vector2f( line.getTo()).sub(new Vector2f( line.getFrom()));
        unitVector.normalize();
        unitVector.x = (unitVector.x != 0) ? 1.0f / unitVector.x : 0f;
        unitVector.y = (unitVector.y != 0) ? 1.0f / unitVector.y : 0f;

        Vector2f min = box.getMin();
        min.sub(line.getFrom()).mul(unitVector);
        Vector2f max = box.getMax();
        max.sub(line.getFrom()).mul(unitVector);

        float tmax = Math.max(Math.min(max.x, min.x), Math.min(max.y, min.y));
        float tmin = Math.min(Math.max(max.x, min.x), Math.max(max.y, min.y));

        if (tmax < 0 || tmax < tmin){
            return false;
        }

        float t = (tmin < 0f) ? tmax : tmin;
        return t > 0f && t * t  < line.lengthSquared();
    }

    //line vs Box2D
    public static boolean lineAndBox2D(Line2D line, Box2D box){
        float deg = box.getRotationDeg();
        Vector2f center = box.getCenter();

        Vector2f localLineStart = new Vector2f(line.getFrom());
        Vector2f localLineEnd = new Vector2f(line.getTo());

        JMath.rotate(localLineStart, deg,center);
        JMath.rotate(localLineEnd, deg,center);

        Line2D localLine = new Line2D(localLineStart, localLineEnd);
        AABB fixedBox = new AABB(box.getMin(), box.getMax());

        return lineAndAABB(localLine, fixedBox);
    }
}
