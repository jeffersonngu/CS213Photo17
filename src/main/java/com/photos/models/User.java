package com.photos.models;

import com.photos.Photos;
import com.photos.utility.PhotosSerializableArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;

/**
 * A model representing a serializable User. Uses the singleton
 * pattern to maintain a single user at all times. Consists
 * of a username, a list of albums, and associated tags.
 */
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The current user instance (singleton pattern). See {@link #generateInstance(String)}.
     * for more information.
     */
    private static User instance = null;

    public static final String STORE_DIR = "user";

    /**
     * The name of the user.
     * We say this is a unique and sufficient identifier for the user as no two users can share a name.
     * Note, admin is a reserved username
     */
    private final String username;

    /**
     * List of albums that the current User {@link #instance} owns
     */
    private final List<Album> albumList;

    /**
     * Map of tags in format {@code <tagname, isMultivalued>}
     */
    private transient ObservableMap<String, Boolean> tagMap;

    /**
     * Use {@link #generateInstance(String)} instead
     * @param username Username of currently logged in user
     */
    private User(String username) {
        this.username = username;
        this.albumList = new PhotosSerializableArrayList<>();
        this.tagMap = FXCollections.observableHashMap();
        this.tagMap.put("person", true);
        this.tagMap.put("location", false);
    }

    /**
     * @return {@link #username}
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return {@link #albumList}
     */
    public List<Album> getAlbums() {
        return albumList;
    }

    /**
     * @return The KeySet of {@link #tagMap} as a Collection
     */
    public Collection<String> getTagCollection() { return tagMap.keySet(); }

    /**
     * @return {@link #tagMap}
     */
    public ObservableMap<String, Boolean> getTagMap() { return tagMap; }

    /**
     * Flatmap all {@link Album} into {@link Photo}, allowing one to
     * search for all photos based on some predicate
     * @param predicate Predicate based on a {@link Photo}
     * @return The list of search results
     */
    public List<Photo> searchPhotos(Predicate<Photo> predicate) {
        return albumList.stream()
                .flatMap(album -> album.getPhotos().stream())
                .distinct()
                .filter(predicate)
                .toList();
    }

    /**
     * Uses the Singleton pattern to only allow one User {@link #instance} to exist at a time
     * @param username Username of currently logged in user
     */
    public synchronized static void generateInstance(String username) {
        if (instance != null) return;
        instance = readUser(username);
    }

    /**
     * @return {@link #instance}
     */
    public static User getInstance() {
        return instance;
    }

    /**
     * Deletes the current {@link #instance} of the singleton.
     * Will need to run {@link #generateInstance(String)} again if
     * need to use {@link #getInstance()}
     */
    public static void deleteInstance() {
        instance = null;
    }

    /**
     * Saves the state of the {@code User} {@link #instance} to a stream
     * (that is, serializes it).
     *
     * @param out the output stream
     * @throws java.io.IOException if an I/O error occurs
     * @serialData The available {@link #tagMap} followed by all of its elements
     *             (each an {@code Object}) in the proper order.
     */
    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(new HashMap<>(tagMap));
        out.defaultWriteObject();
    }

    /**
     * Reconstitutes the {@code User} {@link #instance} from a stream (that is,
     * deserializes it).
     *
     * @param in the input stream
     * @throws ClassNotFoundException if the class of a serialized object
     *         could not be found
     * @throws java.io.IOException if an I/O error occurs
     */
    @SuppressWarnings("unchecked")
    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        tagMap = FXCollections.observableMap((Map<String, Boolean>) in.readObject());
        in.defaultReadObject();
    }

    /**
     * Serializes the current User as dictated by {@link #generateInstance(String)} and {@link #getInstance()}.
     * Will also serialize its {@link Album}s, {@link Photo}s, and available tags
     * @see #readUser(String)
     */
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

    /**
     * Attempts to search for a User from data/user/username.dat to generate the singleton {@link #instance}
     * @param username Username of currently logged in User
     * @return The instance of the saved User or a new empty User if username.dat was not found
     */
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
