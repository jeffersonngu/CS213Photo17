package com.photos.fxml;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

@Deprecated
public class AddTagDialogController {

    @FXML
    public ComboBox<String> tag1;

    @FXML
    public TextField tag2;

    @FXML
    public CheckBox multivalued;

    @FXML
    public Label warning;
}
