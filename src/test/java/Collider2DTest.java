import org.joml.Vector2f;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lwjgl.system.CallbackI;
import org.sid.physics2d.rigidbody.IntersectionDetector2D;
import org.sid.renderer.Line2D;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class Collider2DTest {

    //point vs line
    @Test
    public void PointInLineShouldReturnTrueTest(){
        Line2D line = new Line2D(new Vector2f(2,5), new Vector2f(7,7));

        assertTrue(IntersectionDetector2D.pointOnLine(new  Vector2f(2,5), line ));
    }
    @Test
    public void PointNotInLineShouldReturnFalseTest(){
        Line2D line = new Line2D(new Vector2f(2,5), new Vector2f(7,7));

        Assertions.assertFalse(IntersectionDetector2D.pointOnLine( new  Vector2f(0,0), line));
    }
    @Test
    public void pointOnLine2DShouldReturnTrueTest() {
        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(12, 4));
        Vector2f point = new Vector2f(0, 0);

        assertTrue(IntersectionDetector2D.pointOnLine(point, line ));
    }

    @Test
    public void pointOnLine2DShouldReturnTrueTestTwo() {
        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(12, 4));
        Vector2f point = new Vector2f(12, 4);

        assertTrue(IntersectionDetector2D.pointOnLine(point, line ));
    }

    @Test
    public void pointOnVerticalLineShouldReturnTrue() {
        Line2D line = new Line2D(new Vector2f(0, 0), new Vector2f(0, 10));
        Vector2f point = new Vector2f(0, 5);

        boolean result = IntersectionDetector2D.pointOnLine(point,line);
        assertTrue(result);
    }
}
