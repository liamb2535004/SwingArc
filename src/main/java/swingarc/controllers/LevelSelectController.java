package swingarc.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import swingarc.App;

public class LevelSelectController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}