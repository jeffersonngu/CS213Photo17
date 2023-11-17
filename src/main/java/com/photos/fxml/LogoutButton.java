package com.photos.fxml;

import com.photos.Photos;
import com.photos.User;
import javafx.scene.control.Button;


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
