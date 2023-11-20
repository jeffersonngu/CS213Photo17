package com.photos;

/**
 * Wrapper launcher for {@link PhotosApplication} since Maven's Shade cannot see {@link PhotosApplication#main(String[])}
 * For development purposes, can use either {@link #main(String[])} or {@link PhotosApplication#main(String[])}
 */
public class Launcher {
    
    public static void main(String[] args) {
        PhotosApplication.main(args);
    }
}
