package com.photos.fxml;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class SearchDialogController {

    @FXML
    public ChoiceBox<String> combination1;

    @FXML
    public ChoiceBox<String> combination2;

    @FXML
    public DatePicker date1;

    @FXML
    public DatePicker date2;

    @FXML
    public ComboBox<String> tag1_1;

    @FXML
    public TextField tag1_2;

    @FXML
    public ComboBox<String> tag2_1;

    @FXML
    public TextField tag2_2;
}
