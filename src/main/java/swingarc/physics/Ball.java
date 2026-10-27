package swingarc.physics;

import swingarc.environment.Vector2D;

/**
 * Golf ball state for the physics simulation.
 * Position/velocity use screen coordinates where +Y points downward.
 *
 * @author Kian Dehghani
 */
public class Ball {
    private final Vector2D position;
    private final Vector2D velocity;
    private double radius;
    private boolean moving;
    private boolean grounded;
    private double surfaceFriction;

    public Ball() {
        this(new Vector2D(), 8.0);
    }

    public Ball(Vector2D position, double radius) {
        this.position = new Vector2D(position);
        this.velocity = new Vector2D();
        this.radius = radius;
        this.moving = false;
        this.grounded = true;
        this.surfaceFriction = 0.0;
    }

    public Ball(double x, double y, double radius) {
        this(new Vector2D(x, y), radius);
    }

    public Vector2D getPosition() {
        return position;
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public boolean isGrounded() {
        return grounded;
    }

    public void setGrounded(boolean grounded) {
        this.grounded = grounded;
    }

    public double getSurfaceFriction() {
        return surfaceFriction;
    }

    /**
     * Friction coefficient of the surface currently under the ball.
     * Collision handling will set this from terrain / obstacles later.
     */
    public void setSurfaceFriction(double surfaceFriction) {
        this.surfaceFriction = surfaceFriction;
    }

    public void setPosition(double x, double y) {
        this.position.set(x, y);
    }

    public void setVelocity(double x, double y) {
        this.velocity.set(x, y);
    }

    /**
     * Instantly stop the ball (velocity cleared, no longer moving).
     */
    public void stop() {
        this.velocity.set(0.0, 0.0);
        this.moving = false;
    }

    /**
     * Launch the ball with the given velocity (pixels per second).
     */
    public void launch(Vector2D launchVelocity) {
        this.velocity.set(launchVelocity);
        this.moving = true;
        this.grounded = false;
    }

    public void launch(double vx, double vy) {
        launch(new Vector2D(vx, vy));
    }

    @Override
    public String toString() {
        return "Ball{" +
                "position=" + position +
                ", velocity=" + velocity +
                ", radius=" + radius +
                ", moving=" + moving +
                ", grounded=" + grounded +
                '}';
    }
}
