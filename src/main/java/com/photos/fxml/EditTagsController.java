package com.photos.fxml;

import com.photos.Photo;
import com.photos.Photos;
import com.photos.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.InputEvent;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

public class EditTagsController {

    @FXML
    protected Label addTagLabel;

    @FXML
    protected Label deleteTagLabel;

    @FXML
    protected ComboBox<String> tag1;

    @FXML
    protected TextField tag2;

    @FXML
    protected CheckBox multivalued;

    @FXML
    protected Label warning;

    @FXML
    protected ListView<Map.Entry<String, String>> tagsListView;

    @FXML
    Button backButton;

    protected Photo photo;

    protected Map<String, List<String>> tagsMap;

    public void setupEditTags(Photo self) {
        this.photo = self;
        this.tagsMap = self.getTagsMap();

        /* Display each value in format "key: value" */
        tagsListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Map.Entry<String, String> entry, boolean empty) {
                super.updateItem(entry, empty);
                if (empty || entry == null) {
                    setText(null);
                } else {
                    setText(entry.getKey() + ": " + entry.getValue());
                }
            }
        });
        updateTagsListView();

        tag1.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(String key, boolean empty) {
                super.updateItem(key, empty);
                if (empty || key == null) {
                    setText(key);
                } else {
                    setText(User.getInstance().getTagMap().getOrDefault(key, true) ? ("[MV]: " + key) : ("[SV]: " + key));
                }
            }
        });

        tag1.setItems(FXCollections.observableArrayList(User.getInstance().getTagCollection()));
        tag1.getEditor().textProperty().addListener((observable, oldValue, newValue) -> checkWarnings(newValue));
    }

    /**
     * We do not have the ability to easily use Observable Objects here.
     * It is better to simply clear and repopulate the ListView
     */
    private void updateTagsListView() {
        tagsListView.getItems().clear();
        /* Flatten our map and input each entry into the ListVIew */
        tagsMap.forEach((key, values) -> values.forEach(value -> tagsListView.getItems().add(Map.entry(key, value))));
    }

    private void checkWarnings(String value) {
        if (value == null || value.isBlank()) {
            multivalued.setVisible(false);
            warning.setVisible(false);
            return;
        }

        if (!User.getInstance().getTagMap().containsKey(value)) {
            multivalued.setVisible(true);
        } else {
            multivalued.setVisible(false);
            multivalued.setSelected(false);
        }

        warning.setVisible(!User.getInstance().getTagMap().getOrDefault(value, true) && tagsMap.get(value) != null);
    }

    @FXML
    protected void onAddTag() {
        String tag1Value = tag1.getValue();
        String tag2Value = tag2.getText();
        if (tag1 != null && !tag1Value.isBlank() && !tag2Value.isBlank()) {
            if (!User.getInstance().getTagMap().containsKey(tag1Value)) {
                User.getInstance().getTagMap().put(tag1Value, multivalued.isSelected());
                tag1.setItems(FXCollections.observableArrayList(User.getInstance().getTagCollection()));
            }
            photo.addTag(tag1Value, tag2Value);
            updateTagsListView();
        }
        checkWarnings(tag1Value);
    }

    @FXML
    protected void onDeleteTag() {
        Map.Entry<String, String> entry = tagsListView.getSelectionModel().getSelectedItem();
        if (entry == null) {
            deleteTagLabel.setText("Please select a tag type-value pair from the list first!");
        } else {
            deleteTagLabel.setText("Deleted tag " + entry.getKey() + ": " + entry.getValue() + "!");
            tagsMap.get(entry.getKey()).remove(entry.getValue());
            updateTagsListView();
        }
    }

    @FXML
    protected void onBackToAlbum() {
        Stage currentStage = (Stage) backButton.getScene().getWindow();
        currentStage.close();
    }
}
