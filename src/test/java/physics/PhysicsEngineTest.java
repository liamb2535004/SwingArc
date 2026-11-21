package physics;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import swingarc.environment.Obstacle;
import swingarc.environment.TerrainSegment;
import swingarc.environment.TerrainType;
import swingarc.physics.Ball;
import swingarc.physics.PhysicsEngine;

import static org.junit.jupiter.api.Assertions.*;

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

        assertTrue(ball.getVelocity().getY() > vyBefore);
        assertTrue(ball.getPosition().getY() > 100.0);
    }

    @Test
    public void windPushesBallHorizontallyInAir() {
        ball.launch(0, 0);

        engine.update(ball, 0.2, 2.0);

        assertTrue(ball.getVelocity().getX() > 0.0);
    }

    @Test
    public void frictionSlowsGroundedBall() {
        ball.launch(200, 0);
        ball.setGrounded(true);
        ball.setSurfaceFriction(TerrainType.FAIRWAY.getFriction());

        double speedBefore = ball.getVelocity().magnitude();
        engine.update(ball, 0.5, 0.0);
        double speedAfter = ball.getVelocity().magnitude();

        assertTrue(speedAfter < speedBefore || !ball.isMoving());
    }

    @Test
    public void slowBallStopsAutomatically() {
        ball.launch(5, 0);
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

    @Test
    public void updateWithTerrainLandsBallOnGround() {
        List<TerrainSegment> terrain = Collections.singletonList(
                new TerrainSegment(0, 400, 400, 400, TerrainType.FAIRWAY));

        ball.setPosition(100, 50);
        ball.launch(0, 0);

        for (int i = 0; i < 600; i++) {
            engine.update(ball, 0.016, 0.0, terrain, null);
            if (!ball.isMoving()) {
                break;
            }
        }

        assertFalse(ball.isMoving());
        assertTrue(ball.isGrounded());
        assertEquals(400 - ball.getRadius(), ball.getPosition().getY(), 2.0);
        assertFalse(ball.isInWater());
    }

    @Test
    public void updateStopsWhenBallEntersWaterObstacle() {
        Obstacle pond = new Obstacle("WATER", 90, 200, 40, 40, 1.0, 0.0);
        ball.setPosition(100, 100);
        ball.launch(0, 200);

        for (int i = 0; i < 80; i++) {
            engine.update(ball, 0.016, 0.0, null, Collections.singletonList(pond));
            if (ball.isInWater()) {
                break;
            }
        }

        assertTrue(ball.isInWater());
        assertFalse(ball.isMoving());
    }

    @Test
    public void updateDoesNothingWhenBallAlreadyInWater() {
        ball.setPosition(50, 50);
        ball.setInWater(true);
        ball.setMoving(true);
        ball.setVelocity(100, 100);

        engine.update(ball, 1.0, 5.0);

        assertEquals(50.0, ball.getPosition().getX(), 1e-9);
        assertEquals(50.0, ball.getPosition().getY(), 1e-9);
    }
}
