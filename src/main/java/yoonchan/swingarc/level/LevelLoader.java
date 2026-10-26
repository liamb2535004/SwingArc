package yoonchan.swingarc.level;

import yoonchan.swingarc.environment.Level;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Liam
 */
public final class LevelLoader {
    private static final Gson GSON = new Gson();
    private static final String LEVEL_PATH_FORMAT = "/levels/hole%d.json";

    private LevelLoader() {
    }

    /**
     * @param holeNumber The number of the hole to load (e.g., 1 for hole1.json)
     * @return The Level instance
     * @throws IllegalArgumentException If the file cannot be found in resources
     * @throws RuntimeException If JSON parsing fails due to syntax errors
     */
    public static Level loadLevel(int holeNumber) {
        String resourcePath = String.format(LEVEL_PATH_FORMAT, holeNumber);
        
        InputStream in = LevelLoader.class.getResourceAsStream(resourcePath);
        if (in == null) {
            throw new IllegalArgumentException("Level resource not found: " + resourcePath);
        }

        try (Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
            Level level = GSON.fromJson(reader, Level.class);
            if (level == null) {
                throw new IllegalStateException("Failed to parse JSON; result was null for: " + resourcePath);
            }
            return level;
        } catch (JsonSyntaxException e) {
            throw new RuntimeException("Malformed JSON syntax in: " + resourcePath, e);
        } catch (Exception e) {
            throw new RuntimeException("Error reading level file: " + resourcePath, e);
        }
    }
}
