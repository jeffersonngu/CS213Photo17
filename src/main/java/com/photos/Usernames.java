package com.photos;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Usernames implements Serializable {
    public static final String STORE_DIR = "data";
    public static final String STORE_FILE = "usernames.dat";

    private final List<String> usernames;

    public Usernames() {
        usernames = new ArrayList<>();
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public static void writeUsernames(Usernames usernames) throws IOException {
        File file = Paths.get(STORE_DIR, STORE_FILE).toFile();
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(usernames);
        oos.flush();
        oos.close();
    }

    public static Usernames readUsernames() throws IOException, ClassNotFoundException {
        File file = Paths.get(STORE_DIR, STORE_FILE).toFile();
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        Usernames user = (Usernames) ois.readObject();
        ois.close();
        return user;
    }
}
