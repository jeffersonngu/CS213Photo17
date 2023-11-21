package com.photos.fxml;

import com.photos.models.Photo;
import com.photos.models.User;
import com.photos.utility.Utility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.ImageView;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * Tag/Date based search dialog for photos.
 */
public class SearchDialog extends Dialog<List<Photo>> {

    /**
     * Instance of the controller to interface with the fxml
     */
    private final SearchDialogController searchDialogController;

    public SearchDialog() {
        super();

        /* Load the fxml file */
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("search-dialog.fxml"));
        try {
            setDialogPane(fxmlLoader.load());
            this.searchDialogController = fxmlLoader.getController();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        /* Load extras */
        setTitle("Search Photos");
        setHeaderText("Search for a photo based on tags or dates");

        ImageView infoImage = Utility.generateInformationGraphic("""
                Search photos based on 1-2 tags/a date range. Predicates can
                be combined using AND/OR/IGNORE, which will appear in a dropdown
                once any of the rows are filled out.""");
        setGraphic(infoImage);

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        ObservableList<String> tagsList = FXCollections.observableArrayList(User.getInstance().getTagCollection());
        searchDialogController.tag1_1.setItems(tagsList);
        searchDialogController.tag1_1.setEditable(false);
        searchDialogController.tag2_1.setItems(tagsList);
        searchDialogController.tag2_1.setEditable(false);

        searchDialogController.date1.setEditable(false); /* JavaFX will crash if anything is in the box that is not a date */
        searchDialogController.date2.setEditable(false);

        searchDialogController.tag1_1.valueProperty().addListener((observable, oldValue, newValue) -> updateVisibility());
        searchDialogController.tag1_2.textProperty().addListener((observable, oldValue, newValue) -> updateVisibility());
        searchDialogController.tag2_1.valueProperty().addListener((observable, oldValue, newValue) -> updateVisibility());
        searchDialogController.tag2_2.textProperty().addListener((observable, oldValue, newValue) -> updateVisibility());
        searchDialogController.date1.valueProperty().addListener((observable, oldValue, newValue) -> updateVisibility());
        searchDialogController.date2.valueProperty().addListener((observable, oldValue, newValue) -> updateVisibility());

        /* Set results */
        setResultConverter(dialogButton -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            if (data == ButtonBar.ButtonData.OK_DONE) {
                return User.getInstance().searchPhotos(getSearchPredicate());
            }
            return null;
        });
    }

    /**
     * Predicate builder, will go down each row and add it to the predicate under
     * specified operations (e.g. "AND")
     * @return The final predicate after combining all results
     */
    private Predicate<Photo> getSearchPredicate() {
        Predicate<Photo> result = p -> true;

        if (searchDialogController.tag1_1.getValue() != null && !searchDialogController.tag1_2.getText().isBlank()) {
            result = combinePredicate(result, "AND",
                    photo -> photo.hasTag(searchDialogController.tag1_1.getValue(), searchDialogController.tag1_2.getText()));
        }

        if (searchDialogController.tag2_1.getValue() != null && !searchDialogController.tag2_2.getText().isBlank()) {
            result = combinePredicate(result, searchDialogController.combination1.getValue(),
                    photo -> photo.hasTag(searchDialogController.tag2_1.getValue(), searchDialogController.tag2_2.getText()));
        }

        if (searchDialogController.date1.getValue() != null && searchDialogController.date2.getValue() != null) {
            result = combinePredicate(result, searchDialogController.combination2.getValue(),
                    photo -> photo.getLastModified().to(TimeUnit.DAYS) >= searchDialogController.date1.getValue().toEpochDay()
                            && photo.getLastModified().to(TimeUnit.DAYS) <= searchDialogController.date2.getValue().toEpochDay());
        }

        return result;
    }

    /**
     * Helper function to combine predicates based on an operator
     * @param predicate1 The initial predicate
     * @param operator The operator to combine with (e.g. "AND", "OR")
     * @param predicate2 The predicate to add to predicate1
     * @return
     */
    private Predicate<Photo> combinePredicate(Predicate<Photo> predicate1, String operator, Predicate<Photo> predicate2) {
        return switch (operator) {
            case "AND" -> predicate1.and(predicate2);
            case "OR" -> predicate1.or(predicate2);
            default -> predicate1; /* Ignore */
        };
    }

    /**
     * A listener to display or hide the ChoiceBoxes
     * {@link SearchDialogController#combination1} and
     * {@link SearchDialogController#combination2}
     */
    private void updateVisibility() {
        boolean tag1Active = searchDialogController.tag1_1.getValue() != null && !searchDialogController.tag1_2.getText().isBlank();
        boolean tag2Active = searchDialogController.tag2_1.getValue() != null && !searchDialogController.tag2_2.getText().isBlank();
        boolean dateActive = searchDialogController.date1.getValue() != null && searchDialogController.date2.getValue() != null;

        if (tag1Active && tag2Active) {
            if (!searchDialogController.combination1.isVisible()) {
                searchDialogController.combination1.setVisible(true);
                searchDialogController.combination1.setValue("AND");
            }
        } else {
            searchDialogController.combination1.setVisible(false);
            searchDialogController.combination1.setValue("AND");
        }

        if ((tag1Active || tag2Active) && dateActive) {
            if (!searchDialogController.combination2.isVisible()) {
                searchDialogController.combination2.setVisible(true);
                searchDialogController.combination2.setValue("AND");
            }
        } else {
            searchDialogController.combination2.setVisible(false);
            searchDialogController.combination2.setValue("AND");
        }
    }
}
