package com.photos.models;

import com.photos.Photos;
import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * A model representing a serializable Photo. Consists of various
 * properties, e.g. the path, caption, modification date.
 */
public class Photo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * We create a map of tag types to values. This implementation
     * is based off the concept of a MultiMap, such that
     * a single key may have multiple values.
     * However, we limit it to single values depending on
     * {@link User#getTagMap()}
     */
    private final Map<String, List<String>> tags;

    /**
     * The filesystem path to the photo.
     * We say this is a unique and sufficient identifier for the photo as no two photos can share a path.
     */
    private transient Path path;

    /**
     * The FileTime that stores information about last modification of the file
     */
    private transient FileTime lastModified;

    /**
     * A caption for the photo to display alongside it
     */
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

    /**
     * @return {@link #path}
     */
    public Path getPath() {
        return path;
    }

    /**
     * @return {@link #lastModified}
     */
    public FileTime getLastModified() {
        return lastModified;
    }

    /**
     * Do not modify directly, use {@link #setCaption(String)}} or {@link #getCaption()} instead.
     * Else, manually serialize the data after modifications.
     * Typically used to display the album.
     *
     * @return The observable {@link #caption} of the photo
     */
    public SimpleStringProperty getObservableCaption() {
        return caption;
    }

    /**
     * @return {@link #caption}
     */
    public String getCaption() {
        return caption.get();
    }

    /**
     * @param caption Sets {@link #caption}
     */
    public void setCaption(String caption) {
        this.caption.set(caption);
        Photos.serializeData();
    }

    /**
     * Do not modify directly, use {@link #getTags()} or {@link #addTag(String, String)} instead.
     * Is based off the concept of a MultiMap
     *
     * @return The {@link #tags} of tags
     */
    public Map<String, List<String>> getTagsMap() { return tags; }

    /**
     * @return The List of {@link #tags} flattened in string form "Type: Val". Can include duplicate keys due to multivalued nature of map.
     */
    public List<String> getTags() {
        return tags.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream().map(val -> entry.getKey() + ": " + val))
                .toList();
    }

    /**
     * Add a key, value pair represent a tag. Will override
     * single-valued tags and create new tags as necessary.
     *
     * @param tagType The type of tag, use {@link User#getTagMap()} to see if it is single-valued or multivalued
     * @param tagValue The value of a tag, if multivalued then multiple can be given to the same tagType
     * @see #tags
     */
    public void addTag(String tagType, String tagValue) {
        List<String> val = tags.get(tagType);
        if (val == null) {
            tags.put(tagType, new ArrayList<>(Collections.singletonList(tagValue)));
        } else {
            if (!val.contains(tagValue)) {
                if (User.getInstance().getTagMap().getOrDefault(tagType, true)) {
                    val.add(tagValue);
                } else {
                    val.set(0, tagValue);
                }
            }
        }
        Photos.serializeData();
    }

    /**
     * Check for a given type-value tag pair.
     * @param tagType The type of the tag
     * @param tagValue The value associated with the type
     * @return True if the photo contains the tag, otherwise false
     */
    public boolean hasTag(String tagType, String tagValue) {
        return tags.getOrDefault(tagType, Collections.emptyList()).contains(tagValue);
    }

    /**
     * Compares the {@link #path} of the object for equality
     * @param o The object to compare against
     * @return True if the object is an instance of {@code Photo}
     * and shares the same {@link #path}
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Photo) {
            return path.equals(((Photo) o).getPath());
        } else {
            return false;
        }
    }

    /**
     * Saves the state of the {@code Photo} instance to a stream
     * (that is, serializes it).
     *
     * @param out the output stream
     * @throws java.io.IOException if an I/O error occurs
     * @serialData The photo's {@link #path}, {@link #lastModified}, {@link #caption}
     *             followed by all of its elements
     *             (each an {@code Object}) in the proper order.
     */
    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(path.toString());
        out.writeLong(lastModified.to(TimeUnit.SECONDS));
        out.writeUTF(caption.get());
        out.defaultWriteObject();
    }

    /**
     * Reconstitutes the {@code Photo} instance from a stream (that is,
     * deserializes it).
     *
     * @param in the input stream
     * @throws ClassNotFoundException if the class of a serialized object
     *         could not be found
     * @throws java.io.IOException if an I/O error occurs
     */
    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        path = Path.of(in.readUTF());
        long time = in.readLong();
        caption = new SimpleStringProperty(in.readUTF());
        lastModified = FileTime.from(time, TimeUnit.SECONDS);
        in.defaultReadObject();
    }
}
