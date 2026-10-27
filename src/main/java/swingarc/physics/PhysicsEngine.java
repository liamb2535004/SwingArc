package swingarc.physics;

import swingarc.environment.Vector2D;

public class PhysicsEngine {

    public static final double GRAVITY = 980.0;
    public static final double STOP_SPEED = 8.0;
    public static final double WIND_SCALE = 40.0;

    public void update(Ball ball, double dt, double wind) {
        if (ball == null || dt <= 0.0 || !ball.isMoving()) {
            return;
        }

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

        if (velocity.magnitude() < STOP_SPEED) {
            ball.stop();
        }
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
