module yoonchan.swingarc {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens swingarc to javafx.fxml;
    opens swingarc.environment to com.google.gson;
    opens swingarc.level to com.google.gson;
    opens swingarc.physics;

    exports swingarc;
    exports swingarc.environment;
    exports swingarc.physics;
}
