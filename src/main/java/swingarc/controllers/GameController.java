package swingarc.controllers;

import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class GameController extends BaseController {
    private boolean isPaused = false;

    @FXML
    private Pane gamePane;

    @FXML
    private VBox pauseMenu;

    @FXML
    private StackPane root;

    @FXML
    public void initialize() {
        root.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                newValue.setOnKeyPressed(this::togglePauseMenu);
            }
        });

        root.setFocusTraversable(true);
        pauseMenu.mouseTransparentProperty().bind(pauseMenu.visibleProperty().not());
        pauseMenu.setVisible(false);
    }

    @FXML
    void togglePauseMenu(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            if (isPaused) {
                resumeGame();
            } else {
                pauseGame();
            }
        }
    }

    public void pauseGame() {
        System.out.println("Pause!");
        isPaused = true;
        pauseMenu.requestFocus();
        pauseMenu.setVisible(true);
    }

    @FXML
    public void resumeGame() {
        isPaused = false;
        pauseMenu.setVisible(false);
        root.requestFocus();
    }

    @FXML
    public void switchToLevelSelect() {
        masterController.loadScene("/scenes/level-select.fxml", "Level select");
    }

    @FXML
    public void switchToMainMenu() {
        masterController.loadScene("/scenes/main-menu.fxml", "Level select");
    }
}
