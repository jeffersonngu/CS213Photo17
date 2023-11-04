package com.photos.fxml;

import com.photos.Photos;
import javafx.scene.control.Button;


public class LogoutButton extends Button {

    public LogoutButton() {
        super("Logout");
        this.setOnAction(actionEvent -> {
            Photos.serializeData();
            System.out.println("testing logout"); // TODO: Full implementation
        });
    }

}
