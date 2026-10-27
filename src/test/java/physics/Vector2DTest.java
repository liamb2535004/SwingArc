package physics;

import org.junit.jupiter.api.Test;
import swingarc.environment.Vector2D;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kian Dehghani
 */
public class Vector2DTest {

    private static final double EPS = 1e-9;

    @Test
    public void addAndSubtractProduceNewVectors() {
        Vector2D a = new Vector2D(3, 4);
        Vector2D b = new Vector2D(1, 2);

        Vector2D sum = a.add(b);
        Vector2D diff = a.subtract(b);

        assertEquals(4.0, sum.getX(), EPS);
        assertEquals(6.0, sum.getY(), EPS);
        assertEquals(2.0, diff.getX(), EPS);
        assertEquals(2.0, diff.getY(), EPS);
        // originals unchanged
        assertEquals(3.0, a.getX(), EPS);
        assertEquals(4.0, a.getY(), EPS);
    }

    @Test
    public void magnitudeOf345TriangleIs5() {
        Vector2D v = new Vector2D(3, 4);
        assertEquals(5.0, v.magnitude(), EPS);
        assertEquals(25.0, v.lengthSquared(), EPS);
    }

    @Test
    public void normalizeMakesUnitLength() {
        Vector2D v = new Vector2D(3, 4);
        Vector2D unit = v.normalized();

        assertEquals(1.0, unit.magnitude(), EPS);
        assertEquals(0.6, unit.getX(), EPS);
        assertEquals(0.8, unit.getY(), EPS);
        assertEquals(5.0, v.magnitude(), EPS); // original untouched
    }

    @Test
    public void normalizeZeroVectorStaysZero() {
        Vector2D zero = new Vector2D(0, 0);
        assertEquals(0.0, zero.normalized().magnitude(), EPS);
        zero.normalize();
        assertEquals(0.0, zero.getX(), EPS);
        assertEquals(0.0, zero.getY(), EPS);
    }

    @Test
    public void dotProductAndInPlaceOps() {
        Vector2D a = new Vector2D(2, 3);
        Vector2D b = new Vector2D(4, 5);

        assertEquals(23.0, a.dot(b), EPS);

        a.addInPlace(b);
        assertTrue(a.nearlyEquals(new Vector2D(6, 8), EPS));

        a.scaleInPlace(0.5);
        assertTrue(a.nearlyEquals(new Vector2D(3, 4), EPS));
    }
}
