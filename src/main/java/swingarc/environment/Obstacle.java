package swingarc.environment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Liam
 */
public class Obstacle {
    private String type;
    private List<Vector2D> vertices;
    private double x;
    private double y;
    private double width;
    private double height;
    private double friction;
    private double bounce;

    public Obstacle() {
    }

    public Obstacle(String type, double x, double y, double width, double height, double friction, double bounce) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.friction = friction;
        this.bounce = bounce;
    }

    public Obstacle(String type, List<Vector2D> vertices, double friction, double bounce) {
        this.type = type;
        this.vertices = vertices;
        this.friction = friction;
        this.bounce = bounce;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Vector2D> getVertices() {
        if (vertices != null && !vertices.isEmpty()) {
            return vertices;
        }
        if (width > 0 && height > 0) {
            List<Vector2D> rect = new ArrayList<>(4);
            rect.add(new Vector2D(x, y));
            rect.add(new Vector2D(x + width, y));
            rect.add(new Vector2D(x + width, y + height));
            rect.add(new Vector2D(x, y + height));
            return rect;
        }
        return vertices;
    }

    public void setVertices(List<Vector2D> vertices) {
        this.vertices = vertices;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getFriction() {
        return friction;
    }

    public void setFriction(double friction) {
        this.friction = friction;
    }

    public double getBounce() {
        return bounce;
    }

    public void setBounce(double bounce) {
        this.bounce = bounce;
    }

    /**
     * @param pointX x-axis of a point
     * @param pointY y-axis of a point
     * @return true if point is within bounds of the Obstacle,
     * false if point is outside bounds of Obstacle.
     */
    public boolean contains(double pointX, double pointY) {
        if (vertices != null && vertices.size() >= 3) {
            boolean inside = false;
            for (int i = 0, j = vertices.size() - 1; i < vertices.size(); j = i++) {
                double xi = vertices.get(i).getX();
                double yi = vertices.get(i).getY();
                double xj = vertices.get(j).getX();
                double yj = vertices.get(j).getY();

                boolean intersect = ((yi > pointY) != (yj > pointY))
                        && (pointX < (xj - xi) * (pointY - yi) / (yj - yi + 1e-9) + xi);
                if (intersect) {
                    inside = !inside;
                }
            }
            return inside;
        }
        return pointX >= x && pointX <= (x + width) &&
                pointY >= y && pointY <= (y + height);
    }

    /**
     * Checks if a circular golf ball overlaps or touches this obstacle.
     */
    public boolean intersectsCircle(double centerX, double centerY, double radius) {
        if (contains(centerX, centerY)) {
            return true;
        }
        List<Vector2D> poly = getVertices();
        if (poly == null || poly.size() < 3) {
            return false;
        }
        double radiusSq = radius * radius;
        for (int i = 0; i < poly.size(); i++) {
            Vector2D a = poly.get(i);
            Vector2D b = poly.get((i + 1) % poly.size());
            if (distanceSquaredToSegment(centerX, centerY, a.getX(), a.getY(), b.getX(), b.getY()) <= radiusSq) {
                return true;
            }
        }
        return false;
    }

    private double distanceSquaredToSegment(double px, double py, double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double lenSq = dx * dx + dy * dy;
        if (lenSq == 0.0) {
            return (px - x1) * (px - x1) + (py - y1) * (py - y1);
        }
        double t = Math.max(0.0, Math.min(1.0, ((px - x1) * dx + (py - y1) * dy) / lenSq));
        double projX = x1 + t * dx;
        double projY = y1 + t * dy;
        return (px - projX) * (px - projX) + (py - projY) * (py - projY);
    }

    @Override
    public String toString() {
        return "Obstacle{" +
                "type='" + type + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", friction=" + friction +
                ", bounce=" + bounce +
                '}';
    }
}