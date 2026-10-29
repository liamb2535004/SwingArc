package swingarc.environment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Liam
 */
public class Level {
    private int holeNumber;
    private int par;
    private String name;
    private double wind;
    private Vector2D teePosition;
    private Vector2D cupPosition;
    private List<TerrainSegment> terrain;
    private List<Obstacle> obstacles;

    public Level() {
        this.terrain = new ArrayList<>();
        this.obstacles = new ArrayList<>();
    }

    public Level(int holeNumber, int par, String name, double wind, 
                 Vector2D teePosition, Vector2D cupPosition, 
                 List<TerrainSegment> terrain, List<Obstacle> obstacles) {
        this.holeNumber = holeNumber;
        this.par = par;
        this.name = name;
        this.wind = wind;
        this.teePosition = teePosition;
        this.cupPosition = cupPosition;
        this.terrain = (terrain != null) ? terrain : new ArrayList<>();
        this.obstacles = (obstacles != null) ? obstacles : new ArrayList<>();
    }

    public int getHoleNumber() {
        return holeNumber;
    }

    public void setHoleNumber(int holeNumber) {
        this.holeNumber = holeNumber;
    }

    public int getPar() {
        return par;
    }

    public void setPar(int par) {
        this.par = par;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWind() {
        return wind;
    }

    public void setWind(double wind) {
        this.wind = wind;
    }

    public Vector2D getTeePosition() {
        return teePosition;
    }

    public void setTeePosition(Vector2D teePosition) {
        this.teePosition = teePosition;
    }

    public Vector2D getCupPosition() {
        return cupPosition;
    }

    public void setCupPosition(Vector2D cupPosition) {
        this.cupPosition = cupPosition;
    }

    public List<TerrainSegment> getTerrain() {
        return terrain;
    }

    public void setTerrain(List<TerrainSegment> terrain) {
        this.terrain = terrain;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public void setObstacles(List<Obstacle> obstacles) {
        this.obstacles = obstacles;
    }

    @Override
    public String toString() {
        return "Level{" +
                "holeNumber=" + holeNumber +
                ", par=" + par +
                ", name='" + name + '\'' +
                ", wind=" + wind +
                ", teePosition=" + teePosition +
                ", cupPosition=" + cupPosition +
                ", terrainCount=" + (terrain != null ? terrain.size() : 0) +
                ", obstaclesCount=" + (obstacles != null ? obstacles.size() : 0) +
                '}';
    }
}
