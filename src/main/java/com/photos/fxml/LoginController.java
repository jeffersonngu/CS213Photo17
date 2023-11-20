package com.photos.fxml;

import com.photos.PhotosApplication;
import com.photos.models.User;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * Controller for the first visible screen
 */
public class LoginController {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label signinText;

    @FXML
    private TextField usernameInput;

    @FXML
    protected void onLogin() {
        if (usernameInput == null || usernameInput.getText().isEmpty()) {
            signinText.setText("Please input a username!");
        } else if (!PhotosApplication.getUsernames().contains(usernameInput.getText().toLowerCase())
            && !usernameInput.getText().equalsIgnoreCase("admin")) { /* Invalid Login */
            signinText.setText("Invalid username!");
        } else {
            String username = usernameInput.getText().toLowerCase();
            signinText.setText("Welcome " + username + "!");

            FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), anchorPane);
            fadeTransition.setFromValue(1);
            fadeTransition.setToValue(0);

            fadeTransition.setOnFinished(e -> {
                if (username.equals("admin")) {
                    PhotosApplication.switchScene("admin.fxml");
                } else {
                    User.generateInstance(username);
                    PhotosApplication.switchScene("album-list.fxml");
                }
            });
            fadeTransition.play();
        }
    }

    /**
     * Allows user to hit "Enter" key to login instead of Login button
     * @param event Keyboard event
     */
    @FXML
    protected void onUsernameKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            onLogin();
        }
    }
}
