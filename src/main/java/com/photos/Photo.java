package com.photos;

import java.io.Serial;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.*;

public class Photo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Map<String, List<String>> tags; // Fake MultiMap
    private Path file;
    private Calendar date;
    private String location;
    private String caption;

    public Photo(Path file, Calendar date, String location, String caption) {
        tags = new HashMap<>();

        this.file = file;

        this.date = date;
        this.date.set(Calendar.MILLISECOND, 0);

        this.location = location;
        this.caption = caption;
    }

    public boolean hasTag(String tagName, String tagValue) {
        return tags.getOrDefault(tagName, Collections.emptyList()).contains(tagValue);
    }
}
