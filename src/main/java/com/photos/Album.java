package com.photos;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Album implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final List<Photo> photoList;

    private final String name;

    public Album(String name) {
        photoList = new ArrayList<>();
        this.name = name;
    }

    public List<Photo> getPhotos() {
        return photoList;
    }

    public String getName() { return name; }
}
