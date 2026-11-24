package swingarc.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class EndScreenController extends BaseController {
    @FXML
    void advanceHole(ActionEvent event) {
        // TODO for Liam: Link this up
    }

    @FXML
    void returnToMenu(ActionEvent event) {
        masterController.loadScene("/scenes/main-menu.fxml", "Main menu");
    }
}
