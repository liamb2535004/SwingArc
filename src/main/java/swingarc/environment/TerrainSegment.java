package swingarc.environment;

/**
 *
 * @author Liam
 */

// defines the "floor" of levels through a chain of segments with
// start and end coordinates and a TerrainType.

// Yoon will use these segments to create polygons and fill with
//the appropriate color.

//Kian will use the segments to determine the "floor" and set collisions

// TODO: add method for rounded segments?

public class TerrainSegment {
    private Vector2D start;
    private Vector2D end;
    private TerrainType type;

    public TerrainSegment() {
    }

    public TerrainSegment(Vector2D start, Vector2D end, TerrainType type) {
        this.start = start;
        this.end = end;
        this.type = type;
    }

    //coordinates are parsed into Vector2D object automatically
    public TerrainSegment(double startX, double startY, double endX, double endY, TerrainType type) {
        this(new Vector2D(startX, startY), new Vector2D(endX, endY), type);
    }

    public Vector2D getStart() {
        return start;
    }

    public void setStart(Vector2D start) {
        this.start = start;
    }

    public Vector2D getEnd() {
        return end;
    }

    public void setEnd(Vector2D end) {
        this.end = end;
    }

    public TerrainType getType() {
        return type;
    }

    public void setType(TerrainType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "TerrainSegment{" +
                "start=" + start +
                ", end=" + end +
                ", type=" + type +
                '}';
    }
}
