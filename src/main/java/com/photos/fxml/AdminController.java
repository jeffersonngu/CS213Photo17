package com.photos.fxml;

import com.photos.Photos;
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
    private Label addUserLabel;

    @FXML
    private TextField addUserField;

    @FXML
    private Label deleteUserLabel;

    @FXML
    private ListView<String> usersListView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usersListView.setItems(Photos.getUsernames());
    }

    @FXML
    protected void onAddUser() {
        if (addUserField == null || addUserField.getText().isEmpty()) {
            addUserLabel.setText("Please enter a Username for this User!");
        } else if (Photos.getUsernames().contains(addUserField.getText())) {
            addUserLabel.setText("User already exists!");
        } else {
            addUserLabel.setText("Added " + addUserField.getText() + "!");
            Photos.getUsernames().add(addUserField.getText());
            // TODO: Create equivalent User as well
        }
    }

    @FXML
    protected void onDeleteUser() {
        String selectedUser = usersListView.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            deleteUserLabel.setText("Please select a User from the list first!");
        } else {
            deleteUserLabel.setText("Deleted " + selectedUser + "!");
            Photos.getUsernames().remove(selectedUser);
            // TODO: Delete equivalent User as well
        }
    }

}
