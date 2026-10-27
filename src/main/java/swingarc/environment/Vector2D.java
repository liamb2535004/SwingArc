package swingarc.environment;

/**
 * Basic 2D vector math used by level data and the physics engine.
 *
 * @author Kian Dehghani
 */
public class Vector2D {
    private double x;
    private double y;

    public Vector2D() {
        this(0.0, 0.0);
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2D other) {
        this(other.x, other.y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void set(Vector2D other) {
        this.x = other.x;
        this.y = other.y;
    }

    public Vector2D copy() {
        return new Vector2D(this.x, this.y);
    }

    public Vector2D add(Vector2D other) {
        return new Vector2D(this.x + other.x, this.y + other.y);
    }

    public Vector2D subtract(Vector2D other) {
        return new Vector2D(this.x - other.x, this.y - other.y);
    }

    public Vector2D multiply(double scalar) {
        return new Vector2D(this.x * scalar, this.y * scalar);
    }

    /**
     * Adds another vector into this one (mutates). Useful for integrating velocity.
     */
    public Vector2D addInPlace(Vector2D other) {
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    /**
     * Scales this vector in place.
     */
    public Vector2D scaleInPlace(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    public double magnitude() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public double lengthSquared() {
        return this.x * this.x + this.y * this.y;
    }

    public double distance(Vector2D other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double dot(Vector2D other) {
        return this.x * other.x + this.y * other.y;
    }

    /**
     * Returns a new unit-length vector in the same direction.
     * Zero vector returns a zero vector.
     */
    public Vector2D normalized() {
        double mag = magnitude();
        if (mag == 0.0) {
            return new Vector2D(0.0, 0.0);
        }
        return new Vector2D(this.x / mag, this.y / mag);
    }

    /**
     * Normalizes this vector in place. Zero vector is left unchanged.
     */
    public Vector2D normalize() {
        double mag = magnitude();
        if (mag != 0.0) {
            this.x /= mag;
            this.y /= mag;
        }
        return this;
    }

    public Vector2D negate() {
        return new Vector2D(-this.x, -this.y);
    }

    public boolean nearlyEquals(Vector2D other, double epsilon) {
        return Math.abs(this.x - other.x) <= epsilon
                && Math.abs(this.y - other.y) <= epsilon;
    }

    @Override
    public String toString() {
        return "Vector2D{" + "x=" + x + ", y=" + y + '}';
    }
}
