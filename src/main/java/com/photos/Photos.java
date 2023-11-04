package com.photos;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Photos extends Application {

    public static final String STORE_DIR = "data";
    private static final String STORE_USERNAMES = "usernames.dat";

    public static Stage mainStage;
    public static ObservableList<String> usernames;

    public static void main(String[] args) {
        usernames = readUsernames();
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        // FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Photos");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        serializeData();
    }

    public static void serializeData() {
        writeUsernames(usernames);
        User.writeUser();
    }

    public static void writeUsernames(ObservableList<String> list) {
        try {
            Path path = Paths.get(STORE_DIR, STORE_USERNAMES);
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()));
            oos.writeObject(new ArrayList<>(list));
            oos.flush();
            oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ObservableList<String> readUsernames() {
        Path path = Paths.get(STORE_DIR, STORE_USERNAMES);
        if (Files.exists(path)) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()));
                List<String> list = (List<String>) ois.readObject();
                return FXCollections.observableArrayList(list);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return FXCollections.observableArrayList();
    }
}
