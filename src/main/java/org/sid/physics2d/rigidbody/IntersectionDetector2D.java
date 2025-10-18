package org.sid.physics2d.rigidbody;

import org.joml.Vector2f;
import org.sid.physics2d.primitives.*;
import org.sid.renderer.Line2D;
import org.sid.utils.JMath;


public class IntersectionDetector2D {

    //==================================================
    // POINT VS PRIMITIVES
    //==================================================


    //point vs line
    public static boolean pointOnLine(Vector2f point, Line2D line) {
        // y = mx+c
        float dx = line.getTo().x - line.getFrom().x;
        float dy = line.getTo().y - line.getFrom().y;

        if (dx == 0f) {
            return JMath.compare(point.x, line.getFrom().x);
        }

        float m = dy / dx;
        float c = line.getFrom().y - m * line.getFrom().x;

        float y = m * point.x + c;

        return point.y == y;

    }

    //point vs circle
    public static boolean pointInCircle(Vector2f point, Circle circle) {
        //Compare radius with distance between center and point
        //distance = sqrt((p.x-c.x)^2 + (p.y-c.y)^2)
        // p= point ; c= center

        float distance = (float) Math.sqrt(Math.pow((point.x - circle.getCenter().x), 2) + Math.pow((point.y - circle.getCenter().y), 2));
        return distance < circle.getRadius();
    }

    //point vs Axis Aligned Bounding Box
    public static boolean pointInAABB(Vector2f point, AABB box) {
        Vector2f min = box.getMin();
        Vector2f max = box.getMax();
        return point.x <= max.x && min.x <= point.x && point.y <= max.y && min.y <= point.y;
    }


    //point vs Box2D
    public static boolean pointInBox2D(Vector2f point, Box2D box) {
        Vector2f min = box.getLocalMin();
        Vector2f max = box.getLocalMax();
        Vector2f rotatedPoint = new Vector2f(point);
        JMath.rotate(rotatedPoint, box.getRotationDeg(), box.getCenter());
        return rotatedPoint.x <= max.x && min.x <= rotatedPoint.x && rotatedPoint.y <= max.y && min.y <= rotatedPoint.y;
    }


    //line vs circle
    public static boolean lineAndCircle(Line2D line, Circle circle) {
        if (pointInCircle(line.getTo(), circle) || pointInCircle(line.getFrom(), circle)) {
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

        return pointInCircle(closestPoint, circle);

    }

    //line vs AABB
    public static boolean lineAndAABB(Line2D line, AABB box) {
        if (pointInAABB(line.getFrom(), box) || pointInAABB(line.getTo(), box)) {
            return false;
        }

        Vector2f unitVector = new Vector2f(line.getTo()).sub(new Vector2f(line.getFrom()));
        unitVector.normalize();
        unitVector.x = (unitVector.x != 0) ? 1.0f / unitVector.x : 0f;
        unitVector.y = (unitVector.y != 0) ? 1.0f / unitVector.y : 0f;

        Vector2f min = box.getMin();
        min.sub(line.getFrom()).mul(unitVector);
        Vector2f max = box.getMax();
        max.sub(line.getFrom()).mul(unitVector);

        float tmax = Math.max(Math.min(max.x, min.x), Math.min(max.y, min.y));
        float tmin = Math.min(Math.max(max.x, min.x), Math.max(max.y, min.y));

        if (tmax < 0 || tmax < tmin) {
            return false;
        }

        float t = (tmin < 0f) ? tmax : tmin;
        return t > 0f && t * t < line.lengthSquared();
    }

    //line vs Box2D
    public static boolean lineAndBox2D(Line2D line, Box2D box) {
        float deg = box.getRotationDeg();
        Vector2f center = box.getCenter();

        Vector2f localLineStart = new Vector2f(line.getFrom());
        Vector2f localLineEnd = new Vector2f(line.getTo());

        JMath.rotate(localLineStart, deg, center);
        JMath.rotate(localLineEnd, deg, center);

        Line2D localLine = new Line2D(localLineStart, localLineEnd);
        AABB fixedBox = new AABB(box.getLocalMin(), box.getLocalMax());

        return lineAndAABB(localLine, fixedBox);
    }

    //=============================
    //Raycast
    //=============================

    //Raycast circle

    public static boolean raycast(Ray ray, Circle circle, RaycastResult result) {
        RaycastResult.reset(result);

        //vector E
        Vector2f originToCircle = new Vector2f(circle.getCenter()).sub(ray.getOrigin());
        float radiusSquared = circle.getRadius() * circle.getRadius();
        float originToCircleLengthSquared = originToCircle.lengthSquared();


        //project vector from origin to direction of the ray

        float a = originToCircle.dot(ray.getDirection());
        float bSquared = originToCircleLengthSquared - (a * a);
        if (radiusSquared - bSquared < 0.0f) {
            return false;
        }

        float f = (float) Math.sqrt(radiusSquared - bSquared);

        float t = 0;
        if (originToCircleLengthSquared < radiusSquared) {
            // Ray origin inside the circle
            t = a + f;
        } else {
            t = a - f;
        }

        if (result != null) {
            Vector2f point = new Vector2f(ray.getDirection()).add(new Vector2f(ray.getDirection()).mul(t));
            Vector2f normal = new Vector2f(point).sub(circle.getCenter());
            normal.normalize();

            result.init(point, normal, t, true);
        }
        return true;
    }


    //Raycast AABB
    public static boolean raycast(Ray ray, AABB box, RaycastResult result) {
        RaycastResult.reset(result);

        Vector2f unitVector = ray.getDirection();
        unitVector.normalize();
        unitVector.x = (unitVector.x != 0) ? 1.0f / unitVector.x : 0f;
        unitVector.y = (unitVector.y != 0) ? 1.0f / unitVector.y : 0f;

        Vector2f min = box.getMin();
        min.sub(ray.getOrigin()).mul(unitVector);
        Vector2f max = box.getMax();
        max.sub(ray.getOrigin()).mul(unitVector);

        float tmax = Math.max(Math.min(max.x, min.x), Math.min(max.y, min.y));
        float tmin = Math.min(Math.max(max.x, min.x), Math.max(max.y, min.y));

        if (tmax < 0 || tmax < tmin) {
            return false;
        }

        float t = (tmin < 0f) ? tmax : tmin;
        boolean hit = t > 0f;
        if (!hit) {
            return false;
        }
        if (result != null) {
            Vector2f point = new Vector2f(ray.getDirection()).add(new Vector2f(ray.getDirection()).mul(t));

            //TODO: I guess its wrong, need more research
            Vector2f normal = new Vector2f(ray.getOrigin()).sub(point);
            normal.normalize();

            result.init(point, normal, t, true);
        }
        return true;
    }

    //Raycast Bax2D
    public static boolean raycast(Ray ray, Box2D box, RaycastResult result) {
        RaycastResult.reset(result);

        Vector2f size = box.getHalfSize();
        Vector2f xAxis = new Vector2f(1, 0);
        Vector2f yAxis = new Vector2f(0, 1);
        JMath.rotate(xAxis, -box.getRotationDeg(), new Vector2f(0, 0));
        JMath.rotate(yAxis, -box.getRotationDeg(), new Vector2f(0, 0));

        Vector2f p = new Vector2f(box.getCenter()).sub(ray.getOrigin());
        // Project the direction of the ray onto each axis of the box
        Vector2f f = new Vector2f(
                xAxis.dot(ray.getDirection()),
                yAxis.dot(ray.getDirection())
        );
        // Next, project p onto every axis of the box
        Vector2f e = new Vector2f(
                xAxis.dot(p),
                yAxis.dot(p)
        );

        float[] tArr = {0, 0, 0, 0};
        for (int i = 0; i < 2; i++) {
            if (JMath.compare(f.get(i), 0)) {
                // If the ray is parallel to the current axis, and the origin of the
                // ray is not inside, we have no hit
                if (-e.get(i) - size.get(i) > 0 || -e.get(i) + size.get(i) < 0) {
                    return false;
                }
                f.setComponent(i, 0.00001f); // Set it to small value, to avoid divide by zero
            }
            tArr[i * 2] = (e.get(i) + size.get(i)) / f.get(i); // tmax for this axis
            tArr[i * 2 + 1] = (e.get(i) - size.get(i)) / f.get(i); // tmin for this axis
        }

        float tmin = Math.max(Math.min(tArr[0], tArr[1]), Math.min(tArr[2], tArr[3]));
        float tmax = Math.min(Math.max(tArr[0], tArr[1]), Math.max(tArr[2], tArr[3]));

        float t = (tmin < 0f) ? tmax : tmin;
        boolean hit = t > 0f; //&& t * t < ray.getMaximum();
        if (!hit) {
            return false;
        }

        if (result != null) {
            Vector2f point = new Vector2f(ray.getOrigin()).add(
                    new Vector2f(ray.getDirection()).mul(t));
            Vector2f normal = new Vector2f(ray.getOrigin()).sub(point);
            normal.normalize();

            result.init(point, normal, t, true);
        }

        return true;
    }

    //===========================================================
    //Circle Vs Primitives
    //===========================================================


    //Circle vs Circle

    public static boolean circleAndCircle(Circle circle1, Circle circle2) {

        Vector2f vecBetweenCenters = new Vector2f(circle1.getCenter()).sub(circle2.getCenter());
        return vecBetweenCenters.lengthSquared() < circle1.getRadius() + circle2.getRadius();
    }

    //Circle Vs AABB
    public static boolean circleAndAABB(Circle circle, AABB box) {
        Vector2f min = box.getMin();
        Vector2f max = box.getMax();

        Vector2f closestPointToCircleCenter = new Vector2f(circle.getCenter());

        if (closestPointToCircleCenter.x < min.x) {
            closestPointToCircleCenter.x = min.x;
        } else if (closestPointToCircleCenter.x > max.x) {
            closestPointToCircleCenter.x = max.x;
        }

        if (closestPointToCircleCenter.y < min.y) {
            closestPointToCircleCenter.y = min.y;
        } else if (closestPointToCircleCenter.y > max.y) {
            closestPointToCircleCenter.y = max.y;
        }

        Vector2f circleToBox = new Vector2f(circle.getCenter()).sub(closestPointToCircleCenter);

        return circleToBox.lengthSquared() <= circle.getRadius() * circle.getRadius();
    }

    //Circle Vs AABB
    public static boolean circleAndBox2D(Circle circle, Box2D box) {
        Vector2f min = new Vector2f();
        Vector2f max = new Vector2f(box.getHalfSize()).mul(2.0f);

        Vector2f r = new Vector2f(circle.getCenter()).sub(box.getCenter());

        JMath.rotate(r, -box.getRotationDeg(), new Vector2f(0, 0));

        Vector2f localCirclePos = new Vector2f(r).add(box.getHalfSize());

        Vector2f closestPointToCircleCenter = new Vector2f(localCirclePos);

        if (closestPointToCircleCenter.x < min.x) {
            closestPointToCircleCenter.x = min.x;
        } else if (closestPointToCircleCenter.x > max.x) {
            closestPointToCircleCenter.x = max.x;
        }

        if (closestPointToCircleCenter.y < min.y) {
            closestPointToCircleCenter.y = min.y;
        } else if (closestPointToCircleCenter.y > max.y) {
            closestPointToCircleCenter.y = max.y;
        }

        Vector2f circleToBox = new Vector2f(localCirclePos).sub(closestPointToCircleCenter);

        return circleToBox.lengthSquared() <= circle.getRadius() * circle.getRadius();

    }

    //===========================================================
    //AABB Vs Primitives
    //===========================================================

    public static boolean AABBAndCircle(AABB box, Circle circle) {
        return circleAndAABB(circle, box);
    }

    //Separating Axis Theorem
    public static boolean AABBAndAABB(AABB box1, AABB box2) {

        Vector2f[] axis = {new Vector2f(1, 0), new Vector2f(0, 1)};

        for (Vector2f axi : axis) {

            if (!overlapOnAxis(box1, box2, axi)) {
                return false;
            }
        }
        return true;
    }

    public static boolean AABBAndBox2D(AABB box1, Box2D box2) {


        Vector2f[] axis = {new Vector2f(1, 0), new Vector2f(0, 1),
                           new Vector2f(1, 0), new Vector2f(0, 1)};


        JMath.rotate(axis[2],box2.getRotationDeg(), new Vector2f(0,0));
        JMath.rotate(axis[3],box2.getRotationDeg(), new Vector2f(0,0));

        for (Vector2f axi : axis) {

            if (!overlapOnAxis(box1, box2, axi)) {
                return false;
            }
        }
        return true;
    }

    //===========================================================
    //S.A.T. Helper functions
    //===========================================================
    public static boolean overlapOnAxis(AABB box1, AABB box2, Vector2f axis) {
        Vector2f box1Interval = getInterval(box1, axis);
        Vector2f box2Interval = getInterval(box2, axis);

        return (box1Interval.x <= box2Interval.y && box1Interval.x <= box1Interval.y);
    }

    public static boolean overlapOnAxis(AABB box1, Box2D box2, Vector2f axis) {
        Vector2f box1Interval = getInterval(box1, axis);
        Vector2f box2Interval = getInterval(box2, axis);

        return (box1Interval.x <= box2Interval.y && box1Interval.x <= box1Interval.y);
    }

    public static boolean overlapOnAxis(Box2D box1, Box2D box2, Vector2f axis) {
        Vector2f box1Interval = getInterval(box1, axis);
        Vector2f box2Interval = getInterval(box2, axis);

        return (box1Interval.x <= box2Interval.y && box1Interval.x <= box1Interval.y);
    }

    public static Vector2f getInterval(AABB box, Vector2f axis) {
        Vector2f result = new Vector2f(0, 0);

        Vector2f min = box.getMin();
        Vector2f max = box.getMax();

        Vector2f[] vertices = {
                new Vector2f(min), new Vector2f(max.x, min.y),
                new Vector2f(max), new Vector2f(min.x, max.y)
        };

        //TODO: Unit Test this

        result.x = vertices[0].dot(axis);
        result.y = vertices[0].dot(axis);


        for (int i = 1; i < vertices.length; i++) {
            float projection = vertices[i].dot(axis);
            result.x = Math.min(result.x, projection);
            result.y = Math.max(result.y, projection);
        }
        return result;
    }

    public static Vector2f getInterval(Box2D box, Vector2f axis) {
        Vector2f result = new Vector2f(0, 0);

        Vector2f[] vertices = box.getVertices();

        //TODO: Unit Test this

        result.x = vertices[0].dot(axis);
        result.y = vertices[0].dot(axis);


        for (int i = 1; i < vertices.length; i++) {
            float projection = vertices[i].dot(axis);
            result.x = Math.min(result.x, projection);
            result.y = Math.max(result.y, projection);
        }
        return result;
    }


}
