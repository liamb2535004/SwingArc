package swingarc;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MasterController {
    private final Stage stage;

    public MasterController(Stage stage) {
        this.stage = stage;
    }

    // Centralized method to switch scenes and inject this master reference
    public void loadScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Passing the master to the newly loaded controller
            Object controller = loader.getController();
            if (controller instanceof BaseController) {
                ((BaseController) controller).setMasterController(this);
            }

            stage.setTitle(title);
            stage.setScene(new Scene(root, 1280, 720));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
