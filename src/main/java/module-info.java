module photos {
    requires javafx.controls;
    requires javafx.fxml;

    exports com.photos;
    opens com.photos to javafx.fxml;

    exports com.photos.fxml;
    opens com.photos.fxml to javafx.fxml;
}