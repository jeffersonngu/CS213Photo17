package photos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class Photos extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

        FXMLLoader fxmlLoader = new FXMLLoader(Photos.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), screenSize.getWidth() * 7/8, screenSize.getHeight() * 7/8);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
