package com.photos;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static User instance = null;

    public static final String STORE_DIR = "user";

    private final String username;
    private final List<Album> albumList;

    private transient ObservableList<String> tagList;

    /**
     * Use {@link #generateInstance(String)} instead
     * @param username Username of currently logged in user
     */
    private User(String username) {
        this.username = username;
        this.albumList = new PhotosSerializableArrayList<>();
        this.tagList = FXCollections.observableArrayList(Arrays.asList("location", "person"));
    }

    public String getUsername() {
        return username;
    }

    public List<Album> getAlbums() {
        return albumList;
    }

    public ObservableList<String> getTagList() { return tagList; }

    public List<Photo> searchPhotos(Predicate<Photo> predicate) {
        return albumList.stream()
                .flatMap(album -> album.getPhotos().stream())
                .distinct()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * Uses the Singleton pattern to only allow one User to exist at a time
     * @param username Username of currently logged in user
     */
    public synchronized static void generateInstance(String username) {
        if (instance != null) return;
        instance = readUser(username);
    }

    public static User getInstance() {
        return instance;
    }

    public static void deleteInstance() {
        instance = null;
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(new ArrayList<>(tagList));
        out.defaultWriteObject();
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        tagList = FXCollections.observableArrayList((List<String>) in.readObject());
        in.defaultReadObject();
    }

    public static void writeUser() {
        try {
            if (instance == null) return;
            Path path = Paths.get(Photos.STORE_DIR, User.STORE_DIR, instance.getUsername() + ".dat");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()));
            oos.writeObject(instance);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static User readUser(String username) {
        Path path = Paths.get(Photos.STORE_DIR, User.STORE_DIR, username + ".dat");
        if (Files.exists(path)) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()));
                User user = (User) ois.readObject();
                ois.close();
                return user;
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return new User(username);
    }

}
