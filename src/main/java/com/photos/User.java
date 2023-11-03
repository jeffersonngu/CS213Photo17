package com.photos;

import java.io.*;
import java.util.List;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public static String storeDir = "data";
    public static String storeFile = "";

    private String username;
    private List<Album> albumList;

    public User(String username) {
        this.username = username;
    }

    public static void writeUser(User user) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
        oos.writeObject(user);
    }

    public static User readApp() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
        return (User) ois.readObject();
    }

}
