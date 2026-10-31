package swingarc.controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;
import swingarc.BaseController;

public class OptionsController extends BaseController {

    @FXML
    private Slider gravitySlider;

    @FXML
    private Text gravityText;

    @FXML
    private Button menuButton;

    @FXML
    private Slider powerSlider;

    @FXML
    private Text powerText;

    @FXML
    private Slider volumeSlider;

    @FXML
    private Text volumeText;

    @FXML
    void returnToMenu(ActionEvent event) {
        masterController.loadScene("/scenes/main-menu.fxml", "Main menu");
    }

    @FXML
    public void initialize() {
        StringBinding gravityBinding = (StringBinding) Bindings.format("%.1fx", gravitySlider.valueProperty());
        gravityText.textProperty().bind(gravityBinding);

        StringBinding powerBinding = (StringBinding) Bindings.format("%.1fx", powerSlider.valueProperty());
        powerText.textProperty().bind(powerBinding);

        StringBinding volumeBinding = (StringBinding) Bindings.format("%.0f", volumeSlider.valueProperty());
        volumeText.textProperty().bind(volumeBinding);
    }
}
