package com.photos;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private static User instance = null;
    private static boolean isGenerated = false;

    public static final String[] STORE_DIR = { "data", "user" };

    private final String username;
    private final List<Album> albumList;

    /**
     * Use {@link #generateInstance(String)} instead
     * @param username Username of currently logged in user
     */
    private User(String username) {
        this.username = username;
        this.albumList = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public List<Album> getAlbums() {
        return albumList;
    }

    public List<Photo> searchPhotos(Predicate<Photo> predicate) {
        return albumList.stream()
                .flatMap(album -> album.getPhotos().stream())
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * Uses the Singleton pattern to only allow one User to exist at a time
     * @param username Username of currently logged in user
     */
    public synchronized static void generateInstance(String username) {
        if (isGenerated) return;
        isGenerated = true;
        instance = readUser(username);
    }

    public static User getInstance() {
        return instance;
    }

    public static void writeUser() {
        try {
            if (instance == null) return;
            Path path = Paths.get(STORE_DIR[0], STORE_DIR[1], instance.getUsername() + ".dat");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()));
            oos.writeObject(instance);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static User readUser(String username) {
        Path path = Paths.get(STORE_DIR[0], STORE_DIR[1], username + ".dat");
        if (Files.exists(path)) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()));
                return (User) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return new User(username);
    }

}
