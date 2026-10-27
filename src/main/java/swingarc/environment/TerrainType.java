package swingarc.environment;

/**
 * Ground / hazard types for a hole. Friction and bounce feed the physics engine.
 *
 * @author Liam
 * @author Kian Dehghani (friction / bounce values)
 */
public enum TerrainType {
    FAIRWAY(0.18, 0.35),
    ROUGH(0.35, 0.25),
    SAND(0.55, 0.08),
    WATER(1.00, 0.00),
    WALL(0.05, 0.70);

    private final double friction;
    private final double bounce;

    TerrainType(double friction, double bounce) {
        this.friction = friction;
        this.bounce = bounce;
    }

    public double getFriction() {
        return friction;
    }

    public double getBounce() {
        return bounce;
    }
}
