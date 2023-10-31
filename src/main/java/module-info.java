module photos {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens photos to javafx.fxml;
    exports photos;
}