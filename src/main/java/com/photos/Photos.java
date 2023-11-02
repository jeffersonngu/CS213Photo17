package com.photos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.*;

public class Photos extends Application {

    public static Stage stage;
    public static Usernames usernames;

    public static void main(String[] args) {
        Path path = Paths.get(Usernames.STORE_DIR, Usernames.STORE_FILE);
        if (Files.exists(path)) {
            try {
                usernames = Usernames.readUsernames();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            usernames = new Usernames();
        }
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        // FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Photos");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws IOException {
        Usernames.writeUsernames(usernames);
    }

    public static void printTest() {
        System.out.println("Testing");
    }

}
