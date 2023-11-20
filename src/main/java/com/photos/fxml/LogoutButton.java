package com.photos.fxml;

import com.photos.PhotosApplication;
import com.photos.models.User;
import javafx.scene.control.Button;


/**
 * A shared logout button component used in multiple
 * other controls e.g {@link AlbumController}.
 */
public class LogoutButton extends Button {

    public LogoutButton() {
        super("Logout");
        this.setOnAction(actionEvent -> {
            PhotosApplication.serializeData();
            User.deleteInstance();
            PhotosApplication.switchScene("login.fxml");
        });
    }

}
