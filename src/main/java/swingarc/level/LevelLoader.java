package swingarc.level;

import com.google.gson.Gson;
import swingarc.controllers.BaseController;
import swingarc.environment.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Controller and rendering engine for levels.
 * Draws the course, hazards, HUD, power gauge, and pause menu onto a JavaFX Canvas.
 * Pulls Json data.
 */
public class LevelLoader extends BaseController {

    private static final Gson GSON = new Gson();

    private static int selectedHole = 1;

    /**
     * Default no-argument constructor required by javafx.fxml.FXMLLoader (prevents crashes).
     * Scene and gameplay fields are initialized in initialize().
     */
    public LevelLoader() {}

    /**
     * @return the currently selected hole number to be loaded.
     */
    public static int getSelectedHole() { return selectedHole; }

    /**
     * Sets the hole number to be loaded when the game scene starts.
     * Values below 1 are set to 1.
     * @param holeNumber the hole number (1 to 9)
     */
    public static void setSelectedHole(int holeNumber) { selectedHole = Math.max(1, holeNumber); }

    /**
     * Parses a hole JSON resource file into a Level object.
     * @param holeNumber the hole number corresponding to hole#.json.
     * @return the parsed Level instance.
     * @throws IllegalArgumentException if the JSON resource file cannot be found.
     * @throws RuntimeException if the JSON cannot be parsed into a valid Level.
     */
    public static Level loadLevel(int holeNumber) {
        String path = "/levels/hole" + holeNumber + ".json";
        InputStream stream = LevelLoader.class.getResourceAsStream(path);
        if (stream == null) {
            throw new IllegalArgumentException("Could not find level resource: " + path);
        }
        try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            Level level = GSON.fromJson(reader, Level.class);
            if (level == null) throw new IllegalStateException("Empty level JSON: " + path);
            return level;
        } catch (Exception e) {
            throw new RuntimeException("Error loading level " + holeNumber, e);
        }
    }
}