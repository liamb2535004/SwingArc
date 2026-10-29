package swingarc.controllers;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import swingarc.App;
import swingarc.BaseController;

public class MainMenuController extends BaseController {

    @FXML
    private Button playButton;
    @FXML
    private Button levelSelectButton;
    @FXML
    private Button optionsButton;
    @FXML
    private Button quitButton;

    @FXML
    private void initialize() {}

    @FXML
    private void switchToGame(ActionEvent event) throws IOException {
        masterController.loadScene("/swingarc/game.fxml", "Game");
    }

    @FXML
    private void switchToLevels(ActionEvent event) throws IOException {
        masterController.loadScene("/swingarc/level-select.fxml", "Level select");
    }

    @FXML
    private void switchToOptions(ActionEvent event) throws IOException {
        masterController.loadScene("/swingarc/options.fxml", "Options");
    }

    @FXML
    private void quit(ActionEvent event) {
        Platform.exit();
    }
}
