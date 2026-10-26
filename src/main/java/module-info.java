module yoonchan.swingarc {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    
    opens yoonchan.swingarc.environment to com.google.gson;
    opens yoonchan.swingarc.level to com.google.gson;
    opens yoonchan.swingarc to javafx.fxml;
    
    exports yoonchan.swingarc;   
}
