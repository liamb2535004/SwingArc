package swingarc.physics;

import swingarc.environment.Vector2D;

/**
 * Advances the ball each frame using gravity, wind, and ground friction.
 * Full wall / terrain bounce resolution is left to CollisionDetector (later).
 *
 * Coordinate system: +X right, +Y down (matches the 1920x1080 level JSON).
 *
 * @author Kian Dehghani
 */
public class PhysicsEngine {

    /** Downward acceleration in pixels / second^2. */
    public static final double GRAVITY = 980.0;

    /** Speed below which a moving ball is considered stopped. */
    public static final double STOP_SPEED = 8.0;

    /** Scales the level wind value into a horizontal acceleration. */
    public static final double WIND_SCALE = 40.0;

    /**
     * Integrate one simulation step.
     *
     * @param ball  ball to update
     * @param dt    elapsed seconds since last frame
     * @param wind  horizontal wind from the level (negative = left, positive = right)
     */
    public void update(Ball ball, double dt, double wind) {
        if (ball == null || dt <= 0.0 || !ball.isMoving()) {
            return;
        }

        Vector2D velocity = ball.getVelocity();

        if (!ball.isGrounded()) {
            // Projectile motion: gravity pulls down (+Y), wind pushes sideways.
            velocity.setY(velocity.getY() + GRAVITY * dt);
            velocity.setX(velocity.getX() + wind * WIND_SCALE * dt);
        } else {
            applyFriction(ball, dt);
        }

        // Euler integration: position += velocity * dt
        Vector2D position = ball.getPosition();
        position.setX(position.getX() + velocity.getX() * dt);
        position.setY(position.getY() + velocity.getY() * dt);

        if (velocity.magnitude() < STOP_SPEED) {
            ball.stop();
            if (ball.isGrounded()) {
                // Keep grounded flag; airborne near-zero speed still stops.
            }
        }
    }

    /**
     * Slow the ball along its current path while it is rolling on a surface.
     * Friction is a coefficient in [0, 1+] where higher means stronger drag.
     */
    void applyFriction(Ball ball, double dt) {
        Vector2D velocity = ball.getVelocity();
        double speed = velocity.magnitude();
        if (speed == 0.0) {
            return;
        }

        double friction = Math.max(0.0, ball.getSurfaceFriction());
        // Exponential-style damping so higher friction stops the ball faster.
        double damping = Math.exp(-friction * 6.0 * dt);
        velocity.scaleInPlace(damping);

        // Kill tiny residual velocity so we don't crawl forever.
        if (velocity.magnitude() < STOP_SPEED) {
            velocity.set(0.0, 0.0);
        }
    }

    /**
     * Convenience launch used by input / tests.
     */
    public void launch(Ball ball, Vector2D launchVelocity) {
        if (ball == null || launchVelocity == null) {
            return;
        }
        ball.launch(launchVelocity);
    }
}
