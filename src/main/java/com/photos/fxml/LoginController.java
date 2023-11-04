package com.photos.fxml;

import com.photos.Photos;
import com.photos.User;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;

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
                try {
                    String sceneType;
                    if (username.equals("admin")) {
                        sceneType = "admin.fxml";
                    } else if (Photos.usernames.contains(username)) {
                        User.generateInstance(username);
                        sceneType = "album-list.fxml";
                    } else {
                        // TODO: Invalid login, leaving as a test
                        sceneType = "hello-view.fxml";
                    }
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(sceneType));
                    Scene scene = new Scene(fxmlLoader.load());
                    Photos.mainStage.setScene(scene);
                    Photos.mainStage.show();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            fadeTransition.play();
        }
    }

    @FXML
    protected void onUsernameKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            onLogin();
        }
    }
}
