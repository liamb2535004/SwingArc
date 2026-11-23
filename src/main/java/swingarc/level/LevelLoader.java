package swingarc.level;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import swingarc.controllers.BaseController;
import swingarc.environment.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
        gameCanvas.widthProperty().addListener((obs, o, n) -> render());
        gameCanvas.heightProperty().addListener((obs, o, n) -> render());

        rootPane.getChildren().setAll(gameCanvas);
        startHole(selectedHole);
        Platform.runLater(this::render);
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
        render();
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

    /**
     * Renders a complete frame of the game world, HUD, power gauge, and
     * pause overlay (if paused) scaled to the current canvas size.
     */
    public void render() {
        if (currentLevel == null || gameCanvas == null) return;

        double w = gameCanvas.getWidth();
        double h = gameCanvas.getHeight();
        if (w <= 0 || h <= 0) {
            if (gameCanvas.getScene() == null || gameCanvas.getScene().getWidth() <= 0) return;
            w = gameCanvas.getScene().getWidth();
            h = gameCanvas.getScene().getHeight();
            gameCanvas.widthProperty().unbind();
            gameCanvas.heightProperty().unbind();
            gameCanvas.setWidth(w);
            gameCanvas.setHeight(h);
        }

        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        gc.save();
        gc.scale(w / VIRTUAL_WIDTH, h / VIRTUAL_HEIGHT);

// Sky
        gc.setFill(Color.web("#DFF0FF"));
        gc.fillRect(0, 0, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

// World
        gc.save();
        drawTerrain(gc, currentLevel.getTerrain());
        gc.restore();

// HUD
        drawTopHUD(gc);
        gc.restore();
    }

    /**
     * Renders the ground polygon, turf borders, and surface bands/labels
     * for TerrainType.WATER and TerrainType.SAND.
     *
     * @param gc the GraphicsContext to draw onto
     * @param segments the list of terrain segments making up the course
     */
    private void drawTerrain(GraphicsContext gc, List<TerrainSegment> segments) {
        if (segments == null || segments.isEmpty()) return;

        int n = segments.size() + 3;
        double[] xs = new double[n];
        double[] ys = new double[n];
        for (int i = 0; i < segments.size(); i++) {
            xs[i] = segments.get(i).getStart().getX();
            ys[i] = segments.get(i).getStart().getY();
        }
        TerrainSegment last = segments.get(segments.size() - 1);
        xs[segments.size()] = last.getEnd().getX();
        ys[segments.size()] = last.getEnd().getY();
        xs[segments.size() + 1] = last.getEnd().getX();
        ys[segments.size() + 1] = VIRTUAL_HEIGHT;
        xs[segments.size() + 2] = segments.get(0).getStart().getX();
        ys[segments.size() + 2] = VIRTUAL_HEIGHT;

        gc.setFill(Color.web("#66BB55"));
        gc.fillPolygon(xs, ys, n);

        gc.setTextAlign(TextAlignment.CENTER);
        for (TerrainSegment seg : segments) {
            double x1 = seg.getStart().getX(), y1 = seg.getStart().getY();
            double x2 = seg.getEnd().getX(), y2 = seg.getEnd().getY();
            TerrainType type = seg.getType() != null ? seg.getType() : TerrainType.FAIRWAY;

            if (type == TerrainType.WATER || type == TerrainType.SAND) {
                boolean isWater = type == TerrainType.WATER;
                gc.setFill(isWater ? Color.web("#3B8AD9") : Color.web("#E6CE7D"));
                gc.fillPolygon(new double[]{x1, x2, x2, x1}, new double[]{y1, y2, y2 + 30, y1 + 30}, 4);
                gc.setFill(isWater ? Color.WHITE : Color.web("#7A6220"));
                gc.setFont(Font.font("Outfit", FontWeight.NORMAL, 16));
                gc.fillText(isWater ? "water" : "sand", (x1 + x2) / 2.0, (y1 + y2) / 2.0 + 20);
            } else {
                gc.setStroke(type == TerrainType.ROUGH ? Color.web("#38A169") : Color.web("#2B6E1F"));
                gc.setLineWidth(4.0);
                gc.strokeLine(x1, y1, x2, y2);
            }
        }
        gc.setTextAlign(TextAlignment.LEFT);
    }

    /**
     * Renders the top HUD bar displaying hole number, par, strokes, wind,
     * total score, and the pause/play toggle button.
     *
     * @param gc the GraphicsContext to draw onto
     */
    private void drawTopHUD(GraphicsContext gc) {
        gc.setFill(Color.web("#2E3744"));
        gc.fillRect(45, 35, VIRTUAL_WIDTH - 90, 95);

        double wind = currentLevel.getWind();
        String windText = (wind > 0 ? "→ " : wind < 0 ? "← " : "") + Math.round(Math.abs(wind)) + " mph";
        String[] labels = {"HOLE", "PAR", "STROKES", "WIND", "SCORE"};
        String[] values = {
                currentLevel.getHoleNumber() + " / " + TOTAL_HOLES,
                String.valueOf(currentLevel.getPar()),
                String.valueOf(strokes),
                windText,
                (totalScore > 0 ? "+" : "") + totalScore
        };

        double colWidth = (VIRTUAL_WIDTH - 260) / 5.0;
        gc.setTextAlign(TextAlignment.CENTER);
        for (int i = 0; i < 5; i++) {
            double cx = 70 + colWidth * (i + 0.5);
            gc.setFill(Color.web("#9AA5B4"));
            gc.setFont(Font.font("Outfit", FontWeight.NORMAL, 16));
            gc.fillText(labels[i], cx, 68);
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Outfit", FontWeight.BOLD, 28));
            gc.fillText(values[i], cx, 105);
        }
        gc.setTextAlign(TextAlignment.LEFT);
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