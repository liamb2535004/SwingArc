package swingarc.physics;

import swingarc.environment.Vector2D;

public class Ball {
    private final Vector2D position;
    private final Vector2D velocity;
    private double radius;
    private boolean moving;
    private boolean grounded;
    private boolean inWater;
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
        this.inWater = false;
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

    public void setSurfaceFriction(double surfaceFriction) {
        this.surfaceFriction = surfaceFriction;
    }

    public boolean isInWater() {
        return inWater;
    }

    public void setInWater(boolean inWater) {
        this.inWater = inWater;
    }

    public void setPosition(double x, double y) {
        this.position.set(x, y);
    }

    public void setVelocity(double x, double y) {
        this.velocity.set(x, y);
    }

    public void stop() {
        this.velocity.set(0.0, 0.0);
        this.moving = false;
    }

    public void launch(Vector2D launchVelocity) {
        this.velocity.set(launchVelocity);
        this.moving = true;
        this.grounded = false;
        this.inWater = false;
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
                ", inWater=" + inWater +
                '}';
    }
}
