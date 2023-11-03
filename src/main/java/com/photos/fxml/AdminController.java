package com.photos.fxml;

import com.photos.Photos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML
    private TextField addUserField;

    @FXML
    private Label deleteUserField;

    @FXML
    private ListView<String> usersListView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usersListView.setItems(Photos.usernames);
    }

    @FXML
    void onAddUser(ActionEvent event) {
        if (addUserField == null || addUserField.getText().isEmpty()) {

        } else {
            Photos.usernames.add(addUserField.getText());
            // Create user
        }
    }

    @FXML
    void onDeleteUser(ActionEvent event) {

    }
}
