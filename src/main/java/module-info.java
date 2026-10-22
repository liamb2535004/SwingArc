module yoonchan.swingarc {
    requires javafx.controls;
    requires javafx.fxml;

    opens swingarc to javafx.fxml;
    exports swingarc;
}
