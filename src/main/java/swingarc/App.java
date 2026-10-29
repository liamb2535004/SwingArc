package swingarc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        Font.loadFont(App.class.getResourceAsStream("fonts/Outfit-Black.ttf"), 16);
        Font.loadFont(App.class.getResourceAsStream("fonts/Outfit-Bold.ttf"), 16);
        Font.loadFont(App.class.getResourceAsStream("fonts/Outfit-Light.ttf"), 16);
        Font.loadFont(App.class.getResourceAsStream("fonts/Outfit-Regular.ttf"), 16);

        MasterController mc = new MasterController(stage);
        mc.loadScene("/swingarc/main-menu.fxml", "Main menu");
    }

    public static void main(String[] args) {
        launch();
    }

}
