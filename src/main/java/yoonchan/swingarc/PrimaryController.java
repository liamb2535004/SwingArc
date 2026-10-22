package yoonchan.swingarc;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Font;

public class PrimaryController {

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
    private void switchDirectToGame(ActionEvent event) {
    }

    @FXML
    private void switchToLevels(ActionEvent event) {
    }

    @FXML
    private void switchToOptions(ActionEvent event) {
    }

    @FXML
    private void quit(ActionEvent event) {
    }
}
