package com.photos.fxml;

import com.photos.Photos;
import com.photos.models.User;
import javafx.scene.control.Button;


/**
 * A shared logout button component used in multiple
 * other controls e.g {@link AlbumController}.
 */
public class LogoutButton extends Button {

    /**
     * Will serialize the data, delete the current user instance,
     * and finally bring the user back to the login screen
     */
    public LogoutButton() {
        super("Logout");
        this.setOnAction(actionEvent -> {
            Photos.serializeData();
            User.deleteInstance();
            Photos.switchScene("login.fxml");
        });
    }

}
