package swingarc.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBase;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import swingarc.SoundEffect;

import java.io.IOException;

public class MasterController {
    private final Stage stage;
    private final SoundEffect buttonClick = new SoundEffect("/sounds/button_click.mp3");

    public MasterController(Stage stage) {
        this.stage = stage;
    }

    /**
     * Centralized method to switch scenes and inject this master reference. Also sets up button sounds.
     *
     * @param fxmlPath The FXML scene to open.
     * @param title    The title of the FXML scene shown in the window.
     */
    public void loadScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Passing the master to the newly loaded controller
            Object controller = loader.getController();
            if (controller instanceof BaseController) {
                ((BaseController) controller).setMasterController(this);
            }

            applyButtonSfx(root);

            stage.setTitle(title);
            stage.setScene(new Scene(root, 1280, 720));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Recursively applies a button sound effect to all button nodes in the FXML scene.
     *
     * @param parent The parent (root) node of the scene.
     */
    private void applyButtonSfx(Parent parent) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            boolean isStandardButton = node instanceof ButtonBase;
            boolean isPseudoButton = node instanceof Shape && node.getStyleClass().contains("pseudo-button");

            if (isStandardButton || isPseudoButton) {
                node.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> buttonClick.play());
            } else if (node instanceof Parent childParent) {
                applyButtonSfx(childParent);
            }
        }
    }
}
