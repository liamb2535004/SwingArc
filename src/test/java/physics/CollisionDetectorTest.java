package physics;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import swingarc.environment.Obstacle;
import swingarc.environment.TerrainSegment;
import swingarc.environment.TerrainType;
import swingarc.environment.Vector2D;
import swingarc.physics.Ball;
import swingarc.physics.CollisionDetector;

import static org.junit.jupiter.api.Assertions.*;

public class CollisionDetectorTest {

    private CollisionDetector detector;
    private Ball ball;

    @BeforeEach
    public void setUp() {
        detector = new CollisionDetector();
        ball = new Ball(100, 100, 8);
    }

    private List<TerrainSegment> flatFairway(double y) {
        return Collections.singletonList(
                new TerrainSegment(0, y, 500, y, TerrainType.FAIRWAY));
    }

    @Test
    public void ballLandingSoftlyBecomesGroundedOnFairway() {
        ball.setPosition(100, 850 - 8 + 2);
        ball.setVelocity(40, 30);
        ball.setMoving(true);
        ball.setGrounded(false);

        detector.resolve(ball, flatFairway(850), null);

        assertTrue(ball.isGrounded());
        assertEquals(850 - 8, ball.getPosition().getY(), 1e-6);
        assertEquals(TerrainType.FAIRWAY.getFriction(), ball.getSurfaceFriction(), 1e-9);
        assertTrue(ball.getVelocity().getY() <= 1e-6);
    }

    @Test
    public void hardImpactBouncesOffFairway() {
        ball.setPosition(100, 850 - 8 + 5);
        ball.setVelocity(0, 400);
        ball.setMoving(true);
        ball.setGrounded(false);

        detector.resolve(ball, flatFairway(850), null);

        assertFalse(ball.isGrounded());
        assertTrue(ball.getVelocity().getY() < 0);
    }

    @Test
    public void waterTerrainSinksBall() {
        List<TerrainSegment> water = Collections.singletonList(
                new TerrainSegment(0, 850, 500, 850, TerrainType.WATER));

        ball.setPosition(100, 850 - 8 + 1);
        ball.setVelocity(10, 20);
        ball.setMoving(true);

        detector.resolve(ball, water, null);

        assertTrue(ball.isInWater());
        assertFalse(ball.isMoving());
    }

    @Test
    public void waterObstacleSinksBall() {
        Obstacle pond = new Obstacle("WATER", 80, 800, 60, 40, 1.0, 0.0);
        ball.setPosition(100, 820);
        ball.setVelocity(0, 50);
        ball.setMoving(true);

        detector.resolve(ball, flatFairway(900), Collections.singletonList(pond));

        assertTrue(ball.isInWater());
        assertFalse(ball.isMoving());
    }

    @Test
    public void sandObstacleAppliesHighFriction() {
        Obstacle sand = new Obstacle("SAND", 90, 840, 40, 20, 0.55, 0.05);
        ball.setPosition(100, 845);
        ball.setVelocity(100, 20);
        ball.setMoving(true);
        ball.setGrounded(false);

        detector.resolve(ball, flatFairway(900), Collections.singletonList(sand));

        assertTrue(ball.isGrounded());
        assertEquals(0.55, ball.getSurfaceFriction(), 1e-9);
        assertFalse(ball.isInWater());
    }

    @Test
    public void wallBouncesBallHorizontally() {
        Obstacle wall = new Obstacle("WALL", 120, 700, 20, 200, 0.05, 0.7);
        ball.setPosition(115, 800);
        ball.setVelocity(200, 0);
        ball.setMoving(true);

        detector.resolve(ball, null, Collections.singletonList(wall));

        assertTrue(ball.getVelocity().getX() < 0);
        assertTrue(ball.getPosition().getX() + ball.getRadius() <= wall.getX() + 1e-6
                || ball.getPosition().getX() < 115);
    }

    @Test
    public void slopingTerrainPlacesBallOnInterpolatedHeight() {
        List<TerrainSegment> ramp = Collections.singletonList(
                new TerrainSegment(0, 900, 200, 700, TerrainType.FAIRWAY));

        ball.setPosition(100, 900);
        ball.setVelocity(0, 50);
        ball.setMoving(true);

        detector.resolve(ball, ramp, null);

        double expectedGroundY = 800.0;
        assertEquals(expectedGroundY - ball.getRadius(), ball.getPosition().getY(), 1e-6);
        assertTrue(ball.isGrounded());
    }

    @Test
    public void isInCupWhenCenterInsideRadius() {
        Vector2D cup = new Vector2D(200, 300);
        ball.setPosition(205, 303);

        assertTrue(detector.isInCup(ball, cup, 14.0));
        assertFalse(detector.isInCup(ball, cup, 2.0));
    }

    @Test
    public void findSegmentAtXPicksCorrectPiece() {
        List<TerrainSegment> chain = Arrays.asList(
                new TerrainSegment(0, 850, 100, 850, TerrainType.FAIRWAY),
                new TerrainSegment(100, 850, 200, 800, TerrainType.ROUGH));

        TerrainSegment hit = detector.findSegmentAtX(chain, 150);
        assertNotNull(hit);
        assertEquals(TerrainType.ROUGH, hit.getType());
        assertNull(detector.findSegmentAtX(chain, 250));
    }
}
