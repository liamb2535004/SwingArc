package physics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import swingarc.environment.TerrainType;
import swingarc.physics.Ball;
import swingarc.physics.PhysicsEngine;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Kian Dehghani
 */
public class PhysicsEngineTest {

    private PhysicsEngine engine;
    private Ball ball;

    @BeforeEach
    public void setUp() {
        engine = new PhysicsEngine();
        ball = new Ball(100, 100, 8);
    }

    @Test
    public void gravityIncreasesDownwardVelocityWhileAirborne() {
        ball.launch(0, 0);
        double vyBefore = ball.getVelocity().getY();

        engine.update(ball, 0.1, 0.0);

        assertTrue(ball.getVelocity().getY() > vyBefore,
                "Gravity should pull the ball downward (+Y)");
        assertTrue(ball.getPosition().getY() > 100.0,
                "Ball should move down after falling");
    }

    @Test
    public void windPushesBallHorizontallyInAir() {
        ball.launch(0, 0);

        engine.update(ball, 0.2, 2.0);

        assertTrue(ball.getVelocity().getX() > 0.0,
                "Positive wind should accelerate the ball to the right");
    }

    @Test
    public void frictionSlowsGroundedBall() {
        ball.launch(200, 0);
        ball.setGrounded(true);
        ball.setSurfaceFriction(TerrainType.FAIRWAY.getFriction());

        double speedBefore = ball.getVelocity().magnitude();
        engine.update(ball, 0.5, 0.0);
        double speedAfter = ball.getVelocity().magnitude();

        // Either slowed, or already stopped by the stop-speed threshold.
        assertTrue(speedAfter < speedBefore || !ball.isMoving());
    }

    @Test
    public void slowBallStopsAutomatically() {
        ball.launch(5, 0); // below STOP_SPEED after one tiny step
        ball.setGrounded(true);
        ball.setSurfaceFriction(TerrainType.SAND.getFriction());

        engine.update(ball, 0.05, 0.0);

        assertFalse(ball.isMoving());
        assertEquals(0.0, ball.getVelocity().magnitude(), 1e-9);
    }

    @Test
    public void updateDoesNothingWhenBallAlreadyStopped() {
        ball.setPosition(50, 50);
        assertFalse(ball.isMoving());

        engine.update(ball, 1.0, 5.0);

        assertEquals(50.0, ball.getPosition().getX(), 1e-9);
        assertEquals(50.0, ball.getPosition().getY(), 1e-9);
    }

    @Test
    public void terrainFrictionValuesAreConfigured() {
        assertTrue(TerrainType.SAND.getFriction() > TerrainType.FAIRWAY.getFriction());
        assertTrue(TerrainType.ROUGH.getFriction() > TerrainType.FAIRWAY.getFriction());
        assertTrue(TerrainType.WALL.getBounce() > TerrainType.SAND.getBounce());
        assertEquals(0.0, TerrainType.WATER.getBounce(), 1e-9);
    }
}
