module yoonchan.swingarc {
    requires javafx.controls;
    requires javafx.fxml;

    opens yoonchan.swingarc to javafx.fxml;
    exports yoonchan.swingarc;
}
