package com.photos.models;

import com.photos.Photos;
import com.photos.utility.PhotosSerializableArrayList;
import com.photos.utility.Utility;
import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.util.List;

/**
 * A model representing a serializable album. Consists of a name
 * and a list of photos.
 */
public class Album implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * A list of all photos belonging to the album
     */
    private final List<Photo> photoList;

    /**
     * The name of the album.
     * We say this is a unique and sufficient identifier for the album as no two albums can share a name.
     */
    private transient SimpleStringProperty name;

    public Album(String name) {
        photoList = new PhotosSerializableArrayList<>();
        this.name = new SimpleStringProperty(name);
    }

    public List<Photo> getPhotos() {
        return photoList;
    }

    /**
     * Do not modify directly, use {@link #rename(String)} or {@link #getName()} instead.
     * Else, manually serialize the data after modifications.
     * Typically used to display the album.
     *
     * @return The observable {@link #name} of the album
     */
    public SimpleStringProperty getObservableName() { return name; }

    /**
     * @return {@link #name}
     */
    public String getName() { return name.get(); }

    /**
     * Attempts to rename the album, blank or already existing names will fail
     * @param newName The new name of the album
     * @return Whether rename operation was successful
     */
    public boolean rename(String newName) {
        if (!newName.isBlank() && Utility.isUniqueAlbumName(getName(), newName)) {
            name.set(newName);
            Photos.serializeData();
            return true;
        }
        return false;
    }

    /**
     * Compares the {@link #name}s of the object for equality
     * @param o The object to compare against
     * @return True if the object is an instance of {@code Album}
     * and shares the same {@link #name}
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Album) {
            return this.getName().equalsIgnoreCase(((Album) o).getName());
        } else {
            return false;
        }
    }

    /**
     * Saves the state of the {@code Album} instance to a stream
     * (that is, serializes it).
     *
     * @param out the output stream
     * @throws java.io.IOException if an I/O error occurs
     * @serialData The album's {@link #name} followed by all of its elements
     *             (each an {@code Object}) in the proper order.
     */
    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(name.get());
        out.defaultWriteObject();
    }

    /**
     * Reconstitutes the {@code Album} instance from a stream (that is,
     * deserializes it).
     *
     * @param in the input stream
     * @throws ClassNotFoundException if the class of a serialized object
     *         could not be found
     * @throws java.io.IOException if an I/O error occurs
     */
    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        name = new SimpleStringProperty(in.readUTF());
        in.defaultReadObject();
    }
}
