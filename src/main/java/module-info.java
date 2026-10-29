module yoonchan.swingarc {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens swingarc to javafx.fxml;
    opens swingarc.environment to com.google.gson;
    opens swingarc.level to com.google.gson;

    exports swingarc;
    exports swingarc.controllers;
    opens swingarc.controllers to javafx.fxml;
}
