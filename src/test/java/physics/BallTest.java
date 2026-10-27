package physics;

import org.junit.jupiter.api.Test;
import swingarc.environment.Vector2D;
import swingarc.physics.Ball;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kian Dehghani
 */
public class BallTest {

    @Test
    public void launchSetsVelocityAndAirborneState() {
        Ball ball = new Ball(100, 200, 8);

        assertFalse(ball.isMoving());
        assertTrue(ball.isGrounded());

        ball.launch(150, -300);

        assertTrue(ball.isMoving());
        assertFalse(ball.isGrounded());
        assertEquals(150.0, ball.getVelocity().getX(), 1e-9);
        assertEquals(-300.0, ball.getVelocity().getY(), 1e-9);
    }

    @Test
    public void stopClearsVelocity() {
        Ball ball = new Ball(0, 0, 8);
        ball.launch(new Vector2D(50, 50));
        ball.stop();

        assertFalse(ball.isMoving());
        assertEquals(0.0, ball.getVelocity().magnitude(), 1e-9);
    }

    @Test
    public void surfaceFrictionCanBeSetForGroundedRolling() {
        Ball ball = new Ball();
        ball.setSurfaceFriction(0.35);
        assertEquals(0.35, ball.getSurfaceFriction(), 1e-9);
    }
}
