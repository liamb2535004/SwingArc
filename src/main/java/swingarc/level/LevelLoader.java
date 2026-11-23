package swingarc.level;

import swingarc.controllers.BaseController;

/**
 * Controller and rendering engine for levels.
 * Draws the course, hazards, HUD, power gauge, and pause menu onto a JavaFX Canvas.
 * Pulls Json data.
 */
public class LevelLoader extends BaseController {
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
}