package com.photos.fxml;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.photos.Photos;
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
    protected void onLogin(ActionEvent event) {
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
                    } else {
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
}
