package swingarc.level;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
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
    private static final double VIRTUAL_WIDTH = 1920.0;
    private static final double VIRTUAL_HEIGHT = 1080.0;
    private static final double BALL_RADIUS = 12.0;
    private static final int TOTAL_HOLES = 9;

    private static int selectedHole = 1;

    @FXML
    private AnchorPane rootPane;
    private Canvas gameCanvas;
    private Level currentLevel;
    private Vector2D ballPos;
    private int strokes = 0;
    private int totalScore = 0;
    private boolean isPaused = false;

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
     * Initializes the game canvas, fixes dimensions to the root pane,
     * sets resize and mouse click listeners, and loads the selected hole.
     */
    @FXML
    private void initialize() {
        if (rootPane == null) return;

        gameCanvas = new Canvas(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        gameCanvas.widthProperty().bind(rootPane.widthProperty());
        gameCanvas.heightProperty().bind(rootPane.heightProperty());

        rootPane.getChildren().setAll(gameCanvas);
        startHole(selectedHole);
    }

    /**
     * Loads the indicated hole, places the ball on the tee, resets per-hole
     * gameplay state, and triggers an initial render.
     *
     * @param holeNumber the 1-based hole number to start
     */
    public void startHole(int holeNumber) {
        selectedHole = Math.max(1, holeNumber);
        currentLevel = loadLevel(selectedHole);

        Vector2D tee = currentLevel.getTeePosition();
        ballPos = (tee != null)
                ? new Vector2D(tee.getX(), tee.getY() - BALL_RADIUS)
                : new Vector2D(200.0, 770.0);

        strokes = 0;
        isPaused = false;
    }

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

    public Canvas getGameCanvas() { return gameCanvas; }

    public Level getCurrentLevel() { return currentLevel; }

    public Vector2D getBallPos() { return ballPos; }

    public void setBallPos(Vector2D ballPos) { this.ballPos = ballPos; }

    public int getStrokes() { return strokes; }
    public void setStrokes(int strokes) { this.strokes = strokes; }

    public int getTotalScore() { return totalScore; }
    public void setTotalScore(int totalScore) { this.totalScore = totalScore; }

    public boolean isPaused() { return isPaused; }
}