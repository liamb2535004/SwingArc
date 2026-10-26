package yoonchan.swingarc.environment;

import java.util.List;

/**
 * @author Liam
 */
public class Level {
    public List<TerrainSegment> terrain;
    public List<Obstacle> obstacles;
    public Vector2D teePosition;
    public Vector2D holePosition;
    public int holeNumber;
    public int par;
    public int windCoef;
    
    
    public Level() {
        
    }
    
}
