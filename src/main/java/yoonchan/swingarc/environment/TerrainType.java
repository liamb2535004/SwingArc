package yoonchan.swingarc.environment;

/**
 * @author Liam
 */
public enum TerrainType {
    // TODO: add neccessary friction/bounce values to TerrainTypes
    FAIRWAY(0.0, 0.0),
    ROUGH(0.0, 0.0),
    SAND(0.0, 0.0),
    WATER(0.0, 0.0),
    WALL(0.0, 0.0);
    
    private final double friction;
    private final double bounce;
    
    TerrainType(double friction, double bounce) {
        this.friction = friction;
        this.bounce = bounce;
    }
    
    public double getFriction() {
        return friction;
    }
    
    public double  getBounce() {
        return bounce;
    }
}

