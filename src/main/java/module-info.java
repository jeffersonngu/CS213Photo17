module photos {
    requires javafx.controls;
    requires javafx.fxml;

    exports com.photos;
    opens com.photos to javafx.fxml;

    exports com.photos.fxml;
    opens com.photos.fxml to javafx.fxml;
    exports com.photos.models;
    opens com.photos.models to javafx.fxml;
    opens com.photos.utility to javafx.fxml;
    exports com.photos.utility;
}