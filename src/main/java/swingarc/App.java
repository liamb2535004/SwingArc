package swingarc;

import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import swingarc.controllers.MasterController;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Font.loadFont(App.class.getResourceAsStream("fonts/Outfit-Black.ttf"), 16);
        Font.loadFont(App.class.getResourceAsStream("fonts/Outfit-Bold.ttf"), 16);
        Font.loadFont(App.class.getResourceAsStream("fonts/Outfit-Light.ttf"), 16);
        Font.loadFont(App.class.getResourceAsStream("fonts/Outfit-Regular.ttf"), 16);

        MasterController mc = new MasterController(stage);
        mc.loadScene("/scenes/main-menu.fxml", "Main menu");
    }

    public static void main(String[] args) {
        launch();
    }

}
