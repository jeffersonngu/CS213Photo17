package com.photos.fxml;

import com.photos.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AddTagDialogController {

    @FXML
    public ComboBox<String> tag1;

    @FXML
    public TextField tag2;
}
