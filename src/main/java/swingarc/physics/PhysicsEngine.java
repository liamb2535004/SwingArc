package swingarc.physics;

import java.util.List;

import swingarc.environment.Level;
import swingarc.environment.Obstacle;
import swingarc.environment.TerrainSegment;
import swingarc.environment.Vector2D;

public class PhysicsEngine {

    public static final double GRAVITY = 980.0;
    public static final double STOP_SPEED = 8.0;
    public static final double WIND_SCALE = 40.0;

    private final CollisionDetector collisionDetector;

    public PhysicsEngine() {
        this(new CollisionDetector());
    }

    public PhysicsEngine(CollisionDetector collisionDetector) {
        this.collisionDetector = collisionDetector != null
                ? collisionDetector
                : new CollisionDetector();
    }

    public CollisionDetector getCollisionDetector() {
        return collisionDetector;
    }

    public void update(Ball ball, double dt, Level level) {
        if (level == null) {
            update(ball, dt, 0.0);
            return;
        }
        update(ball, dt, level.getWind(), level.getTerrain(), level.getObstacles());
    }

    public void update(Ball ball, double dt, double wind,
                       List<TerrainSegment> terrain, List<Obstacle> obstacles) {
        if (ball == null || dt <= 0.0 || !ball.isMoving() || ball.isInWater()) {
            return;
        }

        integrate(ball, dt, wind);
        collisionDetector.resolve(ball, terrain, obstacles);

        if (ball.isInWater()) {
            return;
        }

        if (ball.isGrounded() && ball.getVelocity().magnitude() < STOP_SPEED) {
            ball.stop();
        }
    }

    public void update(Ball ball, double dt, double wind) {
        if (ball == null || dt <= 0.0 || !ball.isMoving() || ball.isInWater()) {
            return;
        }

        integrate(ball, dt, wind);

        if (ball.getVelocity().magnitude() < STOP_SPEED) {
            ball.stop();
        }
    }

    private void integrate(Ball ball, double dt, double wind) {
        Vector2D velocity = ball.getVelocity();

        if (!ball.isGrounded()) {
            velocity.setY(velocity.getY() + GRAVITY * dt);
            velocity.setX(velocity.getX() + wind * WIND_SCALE * dt);
        } else {
            applyFriction(ball, dt);
        }

        Vector2D position = ball.getPosition();
        position.setX(position.getX() + velocity.getX() * dt);
        position.setY(position.getY() + velocity.getY() * dt);
    }

    void applyFriction(Ball ball, double dt) {
        Vector2D velocity = ball.getVelocity();
        double speed = velocity.magnitude();
        if (speed == 0.0) {
            return;
        }

        double friction = Math.max(0.0, ball.getSurfaceFriction());
        double damping = Math.exp(-friction * 6.0 * dt);
        velocity.scaleInPlace(damping);

        if (velocity.magnitude() < STOP_SPEED) {
            velocity.set(0.0, 0.0);
        }
    }

    public void launch(Ball ball, Vector2D launchVelocity) {
        if (ball == null || launchVelocity == null) {
            return;
        }
        ball.launch(launchVelocity);
    }
}
