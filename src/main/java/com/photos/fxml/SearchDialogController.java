package com.photos.fxml;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class SearchDialogController {

    @FXML
    public ChoiceBox<?> combination1;

    @FXML
    public ChoiceBox<?> combination2;

    @FXML
    public DatePicker date1;

    @FXML
    public DatePicker date2;

    @FXML
    public TextField tag1_1;

    @FXML
    public TextField tag1_2;

    @FXML
    public TextField tag2_1;

    @FXML
    public TextField tag2_2;
}
