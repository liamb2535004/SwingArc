package swingarc.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class LevelSelectController extends BaseController {
    @FXML
    private Button menuButton;

    @FXML
    void loadLevel(MouseEvent event) {
    }

    @FXML
    void returnToMenu(ActionEvent event) {
        masterController.loadScene("/scenes/main-menu.fxml", "Main menu");
    }

}