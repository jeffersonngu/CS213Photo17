package com.photos;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Photo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Map<String, List<String>> tags; // Fake MultiMap
    private transient Path path;
    private transient FileTime lastModified;
    private String caption;

    public Photo(Path path) {
        tags = new HashMap<>();

        this.path = path;
        this.caption = "";

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

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        if (!caption.isEmpty()) this.caption = caption;
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
        out.defaultWriteObject();
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        path = Path.of(in.readUTF());
        long time = in.readLong();
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
