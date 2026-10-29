package environment;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import swingarc.environment.Obstacle;
import swingarc.environment.TerrainSegment;
import swingarc.level.LevelLoader;

/**
 * @author Liam
 */
public class LevelLoaderTest {
    @Test
    public void testLoadHole1() {
        swingarc.environment.Level level = LevelLoader.loadLevel(1);

        assertNotNull(level, "Loaded level should not be null");
        assertEquals(1, level.getHoleNumber());
        assertEquals(3, level.getPar());
        assertNotNull(level.getTeePosition(), "Tee position must be defined");
        assertNotNull(level.getCupPosition(), "Cup position must be defined");

        List<TerrainSegment> terrain = level.getTerrain();
        assertNotNull(terrain, "Terrain segment list must not be null");
        assertFalse(terrain.isEmpty(), "Terrain should contain segments");

        for (int i = 0; i < terrain.size() - 1; i++) {
            TerrainSegment current = terrain.get(i);
            TerrainSegment next = terrain.get(i + 1);

            assertEquals(current.getEnd().getX(), next.getStart().getX(), 0.001,
                    "Discontinuity in terrain X coordinates at index " + i);
            assertEquals(current.getEnd().getY(), next.getStart().getY(), 0.001,
                    "Discontinuity in terrain Y coordinates at index " + i);
        }

        assertNotNull(level.getObstacles(), "Obstacles list should not be null");
        assertFalse(level.getObstacles().isEmpty(), "Hole 1 should have test obstacles");
        Obstacle sandTrap = level.getObstacles().get(0);
        assertTrue(sandTrap.contains(sandTrap.getX() + 1, sandTrap.getY() + 1),
                "Obstacle contains() must recognize internal points");
    }
}
