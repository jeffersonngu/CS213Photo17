package com.photos;

/**
 * Wrapper launcher for {@link Photos} since Maven's Shade cannot see {@link Photos#main(String[])}
 * For development purposes, can use either {@link #main(String[])} or {@link Photos#main(String[])}
 */
public class Launcher {
    
    public static void main(String[] args) {
        Photos.main(args);
    }
}
