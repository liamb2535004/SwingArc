package yoonchan.swingarc.environment;

/**
 * @author Liam
 */
public class Obstacle {
    private String type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
     * 
     * @param pointX x-axis of a point
     * @param pointY y-axis of a point
     * @return true if point is within bounds of the Obstacle,
     * false if point is outside bounds of Obstacle.
     */
    public boolean contains(double pointX, double pointY) {
        return pointX >= x && pointX <= (x + width) &&
               pointY >= y && pointY <= (y + height);
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
