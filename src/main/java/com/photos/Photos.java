package com.photos;

import com.photos.models.Album;
import com.photos.models.Photo;
import com.photos.models.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Main JavaFX Application and its entry point.
 */
public class Photos extends Application {

    public static final String STORE_DIR = "data";
    private static final String STORE_USERNAMES = "usernames.dat";

    /**
     * The initial stage, we want to keep this so that we can switch between scenes without creating a new window.
     */
    private static Stage mainStage;

    /**
     * Our list of usernames, we will keep a username.dat file that contains all login credentials.
     * If a matching username is found, we can operate on it or load the respective username.dat file.
     */
    private static ObservableList<String> usernames;

    /**
     * The current album we are working on. This field acts as a context for other methods
     * since we may not know which album is currently opened.
     */
    private static Album currentAlbum = null;

    /**
     * The default image when no image can be found.
     * This is useful for cases like the original file was deleted or moved
     * or when an album has no thumbnail to display.
     */
    private static Image noImage;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * {@inheritDoc} <br>
     *
     * We should initialize some data before we fully start our application.
     * Here we:
     * <ol>
     *  <li>Handle uncaught exceptions</li>
     *  <li>Generate our data and data/user folder</li>
     *  <li>Generate our stock folder and data</li>
     *  <li>Initialize our default {@link #noImage}</li>
     *  <li>Begin deserializing our usernames</li>
     * </ol>
     */
    @Override
    public void init() {
        /* We want to exit our program if any uncaught exception occurs */
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            Platform.exit();
        });

        try {
            Path dir = Paths.get(STORE_DIR, User.STORE_DIR);
            if (Files.notExists(dir)) Files.createDirectories(dir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        usernames = readUsernames();

        if (usernames.isEmpty()) { /* First load and/or data was wiped */
            addStockUser();
        }
    }

    /**
     * {@inheritDoc} <br>
     *
     * We will initialize our stage settings and then open the Login page.
     * @param stage {@inheritDoc}
     */
    @Override
    public void start(Stage stage) {
        mainStage = stage;
        noImage = new Image(String.valueOf(Photos.class.getResource("/com/photos/no-image-icon.png")));
        stage.setTitle("Photos");
        switchScene("login.fxml");
    }

    /**
     * Changes the scene to a FXML file in com.photos.fxml.
     *
     * @param fxmlFile Name of the fxml file as a string. Requires the .fxml extension, e.g. {@code "login.fxml"}
     */
    public static void switchScene(String fxmlFile) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Photos.class.getResource("fxml/" + fxmlFile));
            Scene scene = new Scene(fxmlLoader.load());

            URL potentialCssResource = Photos.class.getResource("css/" + fxmlFile.substring(0, fxmlFile.lastIndexOf('.')) + ".css");
            if (potentialCssResource != null) {
                scene.getStylesheets().add(potentialCssResource.toExternalForm());
            }

            mainStage.setScene(scene);
            mainStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc} <br>
     *
     * When our application stops, we will serialize all of our data.
     */
    @Override
    public void stop() {
        serializeData();
    }

    /**
     * Serializes all the data and saves the state of the application.
     */
    public static void serializeData() {
        writeUsernames(usernames);
        User.writeUser();
    }

    /**
     * @return {@link #mainStage}
     */
    public static Stage getMainStage() {
        return mainStage;
    }

    /**
     * @return {@link #currentAlbum}
     */
    public static Album getCurrentAlbum() { return currentAlbum; }

    /**
     * @param album Sets {@link #currentAlbum}
     */
    public static void setCurrentAlbum(Album album) {
        currentAlbum = album;
    }

    /**
     * @return {@link #usernames}
     */
    public static ObservableList<String> getUsernames() { return usernames; }

    /**
     * @return {@link #noImage}
     */
    public static Image getNoImage() { return noImage; }

    /**
     * Saves a list of known usernames.
     *
     * @param list List of usernames.
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
     * Reads a list of known usernames.
     *
     * @return The {@link ObservableList} of usernames
     * @see Photos#writeUsernames(ObservableList)
     */
    @SuppressWarnings("unchecked")
    public static ObservableList<String> readUsernames() {
        Path path = Paths.get(STORE_DIR, STORE_USERNAMES);
        if (Files.exists(path)) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()));
                List<String> list = (List<String>) ois.readObject();
                ois.close();
                return FXCollections.observableArrayList(list);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return FXCollections.observableArrayList();
    }

    /**
     * Create a new stock user and load the 5 photos in data/stock
     * to its stock album.
     */
    public static void addStockUser() {
        String[] stockImages = {"ablobattention.gif", "ablobguitar.gif", "ablobhop.gif", "ablobhydraulicpress.gif", "ablobnomcookie.gif"};

        try {
            Path dir = Paths.get(STORE_DIR, "stock");
            if (Files.notExists(dir)) Files.createDirectories(dir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        usernames.add("stock");

        /* "Login" the stock user temporarily to add photos */
        User.generateInstance("stock");
        User stockUser = User.getInstance();

        Album stockAlbum = new Album("Stock Album");

        String out = "data/stock/";
        IntStream.range(0, 5).forEach(i -> {
            try {
                Files.copy(Objects.requireNonNull(Photos.class.getResourceAsStream("stock/" + stockImages[i])), Paths.get(out + stockImages[i]), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Photo photo = new Photo(Paths.get("data/stock/" + stockImages[i]));
            photo.setCaption("Stock Photo " + (i + 1));
            stockAlbum.getPhotos().add(photo);
        });

        stockUser.getAlbums().add(stockAlbum);

        /* Serialize changes */
        serializeData();

        /* "Logout" the stock user */
        User.deleteInstance();
    }
}
