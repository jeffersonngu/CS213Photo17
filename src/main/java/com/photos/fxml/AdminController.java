package com.photos.fxml;

import com.photos.PhotosApplication;
import com.photos.models.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML
    private Label addUserLabel;

    @FXML
    private TextField addUserField;

    @FXML
    private Label deleteUserLabel;

    @FXML
    private ListView<String> usersListView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usersListView.setItems(PhotosApplication.getUsernames());
    }

    @FXML
    protected void onAddUser() {
        if (addUserField == null || addUserField.getText().isEmpty()) {
            addUserLabel.setText("Please enter a Username for this User!");
        } else if (PhotosApplication.getUsernames().contains(addUserField.getText().toLowerCase())) {
            addUserLabel.setText("User already exists!");
        } else if (addUserField.getText().equalsIgnoreCase("admin")) {
            addUserLabel.setText("Cannot add that username, reserved for admin!");
        } else {
            addUserLabel.setText("Added " + addUserField.getText() + "!");
            PhotosApplication.getUsernames().add(addUserField.getText());
        }
    }

    @FXML
    protected void onDeleteUser() {
        String selectedUser = usersListView.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            deleteUserLabel.setText("Please select a User from the list first!");
        } else {
            deleteUserLabel.setText("Deleted " + selectedUser + "!");
            PhotosApplication.getUsernames().remove(selectedUser);
            try {
                Files.deleteIfExists(Paths.get(PhotosApplication.STORE_DIR, User.STORE_DIR, selectedUser + ".dat"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
