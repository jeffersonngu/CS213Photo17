package com.photos.fxml;

import com.photos.models.Photo;
import com.photos.models.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

/**
 * Controller of edit-tags.fxml for the edit
 * tags option in the dropdown menu for a single photo.
 */
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
    protected Button backButton;

    /**
     * The photo we are curerntly operating on
     */
    protected Photo photo;

    /**
     * A reference to the tagsMap
     */
    protected Map<String, List<String>> tagsMap;

    /**
     * Initialization setup for the FXML, but since we need context we call this
     * method before fully switching scenes.
     * @param self The photo we are operating on
     */
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
     * It is better to simply clear and repopulate the ListView.
     */
    private void updateTagsListView() {
        tagsListView.getItems().clear();
        /* Flatten our map and input each entry into the ListVIew */
        tagsMap.forEach((key, values) -> values.forEach(value -> tagsListView.getItems().add(Map.entry(key, value))));
    }

    /**
     * Warns the user if they are trying to modify a
     * single-valued tag.
     * Also asks the user if they want to make
     * a new tag single or multivalued.
     * @param value The tag type inputted.
     */
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

    /**
     * Adds a tag-value pair to the {@link #photo}.
     */
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

    /**
     * Deletes the currently selected tag type-value pair from the {@link #photo}.
     */
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

    /**
     * Closes the window.
     */
    @FXML
    protected void onBackToAlbum() {
        Stage currentStage = (Stage) backButton.getScene().getWindow();
        currentStage.close();
    }
}
