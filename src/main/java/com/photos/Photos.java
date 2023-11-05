package com.photos;

import com.photos.fxml.LogoutButton;
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

    private static Stage mainStage;
    private static ObservableList<String> usernames;

    private static Album currentAlbum = null;

    public static void main(String[] args) {
        usernames = readUsernames();
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        stage.setTitle("Photos");
        switchScene("login.fxml");
    }

    /**
     * Changes the scene to a FXML file in com.photos.fxml
     *
     * @param fxmlFile Name of the fxml file as a string. Requires the .fxml extension, e.g. {@code "login.fxml"}
     */
    public static void switchScene(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Photos.class.getResource("fxml/" + fxmlFile));
            mainStage.setScene(new Scene(fxmlLoader.load()));
            mainStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {
        serializeData();
    }

    /**
     * Serializes all the data and saves the state of the application. Should be used during logout
     * @see LogoutButton#LogoutButton()
     */
    public static void serializeData() {
        writeUsernames(usernames);
        User.writeUser();
    }

    /**
     * @return The main stage that controls application
     */
    public static Stage getMainStage() {
        return mainStage;
    }

    /**
     * @return The currently worked on album
     */
    public static Album getCurrentAlbum() { return currentAlbum; }

    /**
     * Sets a new album to work on
     * @param album A new album to work on
     */
    public static void setCurrentAlbum(Album album) {
        currentAlbum = album;
    }

    /**
     * @return An {@link ObservableList} of registered usernames
     */
    public static ObservableList<String> getUsernames() { return usernames; }

    /**
     * Saves a list of known usernames. Required since {@link ObservableList} is not serializable
     *
     * @param list
     * @see Photos#readUsernames()
     */
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

    /**
     * Reads a list of known usernames. Required since {@link ObservableList} is not serializable
     *
     * @return The {@link ObservableList} of usernames
     * @see Photos#writeUsernames(ObservableList)
     */
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
