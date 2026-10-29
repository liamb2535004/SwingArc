package swingarc.controllers;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import swingarc.App;

public class MainMenuController {

    @FXML
    private Button playButton;
    @FXML
    private Button levelSelectButton;
    @FXML
    private Button optionsButton;
    @FXML
    private Button quitButton;

    @FXML
    private void initialize() {
        Font.loadFont(App.class.getResourceAsStream("fonts/Outfit-Black.ttf"), 16);
        Font.loadFont(App.class.getResourceAsStream("fonts/Outfit-Bold.ttf"), 16);
        Font.loadFont(App.class.getResourceAsStream("fonts/Outfit-Light.ttf"), 16);
        Font.loadFont(App.class.getResourceAsStream("fonts/Outfit-Regular.ttf"), 16);
    }

    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void switchToGame(ActionEvent event) throws IOException {
        App.setRoot("game");
    }

    @FXML
    private void switchToLevels(ActionEvent event) throws IOException {
        App.setRoot("level-select");
    }

    @FXML
    private void switchToOptions(ActionEvent event) throws IOException {
        App.setRoot("options");
    }

    @FXML
    private void quit(ActionEvent event) {
        Platform.exit();
    }
}
