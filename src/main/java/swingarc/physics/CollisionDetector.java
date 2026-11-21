package swingarc.physics;

import java.util.List;

import swingarc.environment.Obstacle;
import swingarc.environment.TerrainSegment;
import swingarc.environment.TerrainType;
import swingarc.environment.Vector2D;

public class CollisionDetector {

    public static final double GROUND_EPSILON = 1.5;
    public static final double STICK_SPEED = 60.0;
    public static final double DEFAULT_CUP_RADIUS = 14.0;

    public void resolve(Ball ball, List<TerrainSegment> terrain, List<Obstacle> obstacles) {
        if (ball == null) {
            return;
        }

        if (terrain != null && !terrain.isEmpty()) {
            resolveTerrain(ball, terrain);
            if (ball.isInWater()) {
                return;
            }
        }

        if (obstacles != null && !obstacles.isEmpty()) {
            resolveObstacles(ball, obstacles);
        }
    }

    public boolean isInCup(Ball ball, Vector2D cupPosition) {
        return isInCup(ball, cupPosition, DEFAULT_CUP_RADIUS);
    }

    public boolean isInCup(Ball ball, Vector2D cupPosition, double cupRadius) {
        if (ball == null || cupPosition == null || cupRadius <= 0.0) {
            return false;
        }
        return ball.getPosition().distance(cupPosition) <= cupRadius;
    }

    void resolveTerrain(Ball ball, List<TerrainSegment> terrain) {
        Vector2D position = ball.getPosition();
        TerrainSegment segment = findSegmentAtX(terrain, position.getX());
        if (segment == null) {
            ball.setGrounded(false);
            return;
        }

        double groundY = heightOnSegment(segment, position.getX());
        double bottomY = position.getY() + ball.getRadius();
        Vector2D normal = upwardNormal(segment);
        TerrainType type = segment.getType() != null ? segment.getType() : TerrainType.FAIRWAY;

        if (type == TerrainType.WATER && bottomY >= groundY - GROUND_EPSILON) {
            sinkInWater(ball);
            return;
        }

        if (bottomY >= groundY - GROUND_EPSILON) {
            position.setY(groundY - ball.getRadius());

            Vector2D velocity = ball.getVelocity();
            double intoGround = velocity.dot(normal.negate());

            if (intoGround > STICK_SPEED && type.getBounce() > 0.0) {
                reflect(velocity, normal, type.getBounce());
                ball.setGrounded(false);
            } else {
                removeNormalComponent(velocity, normal);
                ball.setGrounded(true);
                ball.setSurfaceFriction(type.getFriction());
            }
        } else {
            ball.setGrounded(false);
        }
    }

    void resolveObstacles(Ball ball, List<Obstacle> obstacles) {
        Vector2D position = ball.getPosition();
        double r = ball.getRadius();

        for (Obstacle obstacle : obstacles) {
            if (obstacle == null) {
                continue;
            }

            String type = obstacle.getType() == null ? "" : obstacle.getType().trim().toUpperCase();

            if (!circleOverlapsAabb(position.getX(), position.getY(), r,
                    obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight())) {
                continue;
            }

            if ("WATER".equals(type)) {
                sinkInWater(ball);
                return;
            }

            if ("SAND".equals(type) || "SANDTRAP".equals(type)) {
                ball.setSurfaceFriction(Math.max(ball.getSurfaceFriction(), obstacle.getFriction()));
                if (ball.getVelocity().getY() > 0) {
                    ball.getVelocity().setY(ball.getVelocity().getY() * obstacle.getBounce());
                }
                ball.setGrounded(true);
                continue;
            }

            resolveWall(ball, obstacle);
        }
    }

    void resolveWall(Ball ball, Obstacle wall) {
        Vector2D position = ball.getPosition();
        Vector2D velocity = ball.getVelocity();
        double r = ball.getRadius();

        double left = wall.getX();
        double right = wall.getX() + wall.getWidth();
        double top = wall.getY();
        double bottom = wall.getY() + wall.getHeight();

        double closestX = clamp(position.getX(), left, right);
        double closestY = clamp(position.getY(), top, bottom);
        double dx = position.getX() - closestX;
        double dy = position.getY() - closestY;
        double distSq = dx * dx + dy * dy;

        if (distSq == 0.0) {
            double penLeft = position.getX() - left;
            double penRight = right - position.getX();
            double penTop = position.getY() - top;
            double penBottom = bottom - position.getY();
            double minPen = Math.min(Math.min(penLeft, penRight), Math.min(penTop, penBottom));

            double bounce = wall.getBounce() > 0 ? wall.getBounce() : TerrainType.WALL.getBounce();
            if (minPen == penLeft) {
                position.setX(left - r);
                velocity.setX(-Math.abs(velocity.getX()) * bounce);
            } else if (minPen == penRight) {
                position.setX(right + r);
                velocity.setX(Math.abs(velocity.getX()) * bounce);
            } else if (minPen == penTop) {
                position.setY(top - r);
                velocity.setY(-Math.abs(velocity.getY()) * bounce);
            } else {
                position.setY(bottom + r);
                velocity.setY(Math.abs(velocity.getY()) * bounce);
            }
            ball.setGrounded(false);
            return;
        }

        double dist = Math.sqrt(distSq);
        if (dist >= r) {
            return;
        }

        double nx = dx / dist;
        double ny = dy / dist;
        double penetration = r - dist;
        position.setX(position.getX() + nx * penetration);
        position.setY(position.getY() + ny * penetration);

        Vector2D normal = new Vector2D(nx, ny);
        double bounce = wall.getBounce() > 0 ? wall.getBounce() : TerrainType.WALL.getBounce();
        reflect(velocity, normal, bounce);
        ball.setGrounded(false);
    }

    void sinkInWater(Ball ball) {
        ball.setInWater(true);
        ball.setGrounded(false);
        ball.stop();
    }

    public TerrainSegment findSegmentAtX(List<TerrainSegment> terrain, double x) {
        for (TerrainSegment segment : terrain) {
            if (segment == null || segment.getStart() == null || segment.getEnd() == null) {
                continue;
            }
            double x0 = segment.getStart().getX();
            double x1 = segment.getEnd().getX();
            double minX = Math.min(x0, x1);
            double maxX = Math.max(x0, x1);
            if (x >= minX && x <= maxX) {
                return segment;
            }
        }
        return null;
    }

    double heightOnSegment(TerrainSegment segment, double x) {
        Vector2D start = segment.getStart();
        Vector2D end = segment.getEnd();
        double dx = end.getX() - start.getX();
        if (Math.abs(dx) < 1e-9) {
            return start.getY();
        }
        double t = (x - start.getX()) / dx;
        t = clamp(t, 0.0, 1.0);
        return start.getY() + t * (end.getY() - start.getY());
    }

    Vector2D upwardNormal(TerrainSegment segment) {
        double dx = segment.getEnd().getX() - segment.getStart().getX();
        double dy = segment.getEnd().getY() - segment.getStart().getY();
        Vector2D n1 = new Vector2D(-dy, dx);
        Vector2D n2 = new Vector2D(dy, -dx);
        Vector2D chosen = (n1.getY() <= n2.getY()) ? n1 : n2;
        return chosen.normalize();
    }

    void reflect(Vector2D velocity, Vector2D normal, double bounce) {
        double vn = velocity.dot(normal);
        if (vn >= 0.0) {
            return;
        }
        velocity.setX(velocity.getX() - 2.0 * vn * normal.getX());
        velocity.setY(velocity.getY() - 2.0 * vn * normal.getY());
        velocity.scaleInPlace(bounce);
    }

    void removeNormalComponent(Vector2D velocity, Vector2D normal) {
        double vn = velocity.dot(normal);
        if (vn < 0.0) {
            velocity.setX(velocity.getX() - vn * normal.getX());
            velocity.setY(velocity.getY() - vn * normal.getY());
        }
    }

    boolean circleOverlapsAabb(double cx, double cy, double radius,
                               double x, double y, double w, double h) {
        double closestX = clamp(cx, x, x + w);
        double closestY = clamp(cy, y, y + h);
        double dx = cx - closestX;
        double dy = cy - closestY;
        return (dx * dx + dy * dy) <= radius * radius;
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
