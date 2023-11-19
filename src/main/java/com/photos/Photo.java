package com.photos;

import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Photo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Map<String, List<String>> tags; /* Fake MultiMap */
    private transient Path path;
    private transient FileTime lastModified;
    private transient SimpleStringProperty caption;

    public Photo(Path path) {
        tags = new HashMap<>();

        this.path = path;
        this.caption = new SimpleStringProperty("");

        try {
            lastModified = Files.getLastModifiedTime(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Path getPath() {
        return path;
    }

    public FileTime getLastModified() {
        return lastModified;
    }

    /**
     * Do not modify directly, use {@link #setCaption(String)}} or {@link #getCaption()} instead.
     * Else, manually serialize the data after modifications.
     * Typically used to display the album.
     *
     * @return The observable caption of the photo
     */
    public SimpleStringProperty getObservableCaption() {
        return caption;
    }

    public String getCaption() {
        return caption.get();
    }

    public void setCaption(String caption) {
        this.caption.set(caption);
        Photos.serializeData();
    }


    public List<String> getTags() {
        return tags.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream().map(val -> entry.getKey() + ": " + val))
                .toList();
    }

    public void addTag(String tag1, String tag2) {
        List<String> val = tags.get(tag1);
        if (val == null) {
            tags.put(tag1, new ArrayList<>(Collections.singletonList(tag2)));
        } else {
            val.add(tag2);
        }
        Photos.serializeData();
    }

    /**
     * Check for a given name-value tag pair.
     * @param tagName The name of the tag
     * @param tagValue The value associated with the name
     * @return True if the photo contains the tag, otherwise false
     */
    public boolean hasTag(String tagName, String tagValue) {
        return tags.getOrDefault(tagName, Collections.emptyList()).contains(tagValue);
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(path.toString());
        out.writeLong(lastModified.to(TimeUnit.SECONDS));
        out.writeUTF(caption.get());
        out.defaultWriteObject();
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        path = Path.of(in.readUTF());
        long time = in.readLong();
        caption = new SimpleStringProperty(in.readUTF());
        lastModified = FileTime.from(time, TimeUnit.SECONDS);
        in.defaultReadObject();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Photo) {
            return path.equals(((Photo) o).getPath());
        } else {
            return false;
        }
    }
}
