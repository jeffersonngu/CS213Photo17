package com.photos.fxml;

import com.photos.Photos;
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
            Photos.serializeData();
            User.deleteInstance();
            Photos.switchScene("login.fxml");
        });
    }

}
