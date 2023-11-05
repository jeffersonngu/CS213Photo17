package com.photos.fxml;

import com.photos.Photos;
import com.photos.User;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

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
        } else {
            String username = usernameInput.getText().toLowerCase();
            signinText.setText("Welcome " + username + "!");

            FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), anchorPane);
            fadeTransition.setFromValue(1);
            fadeTransition.setToValue(0);

            fadeTransition.setOnFinished(e -> {
                if (username.equals("admin")) {
                    Photos.switchScene("admin.fxml");
                } else if (Photos.getUsernames().contains(username)) {
                    User.generateInstance(username);
                    Photos.switchScene("album-list.fxml");
                } else { /* Invalid Login */
                    signinText.setText("Invalid username!");
                    // TODO: Remove, leaving as a test
                    Photos.switchScene("hello-view.fxml");
                }
            });
            fadeTransition.play();
        }
    }

    /**
     * Allows user to hit "Enter" key to login instead of Login button
     * @param event
     */
    @FXML
    protected void onUsernameKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            onLogin();
        }
    }
}
