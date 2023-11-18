package com.photos;

import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.util.List;

public class Album implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;

    private final List<Photo> photoList;

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
     * @return The observable name of the album
     */
    public SimpleStringProperty getObservableName() { return name; }

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

    @Override
    public boolean equals(Object o) {
        if (o instanceof Album) {
            return this.getName().equalsIgnoreCase(((Album) o).getName());
        } else {
            return false;
        }
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(name.get());
        out.defaultWriteObject();
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        name = new SimpleStringProperty(in.readUTF());
        in.defaultReadObject();
    }
}
