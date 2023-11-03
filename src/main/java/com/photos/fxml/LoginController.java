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
        if (usernameInput == null || usernameInput.getText().equals("")) {
            signinText.setText("Please input a username!");
        } else {
            signinText.setText("Welcome " + usernameInput.getText() + "!");

            FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), anchorPane);
            fadeTransition.setFromValue(1);
            fadeTransition.setToValue(0);

            fadeTransition.setOnFinished(e -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    Photos.stage.setScene(scene);
                    Photos.stage.show();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            fadeTransition.play();
        }
    }
}
