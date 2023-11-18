package com.photos.fxml;

import com.photos.Photo;
import com.photos.User;
import com.photos.Utility;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class SearchDialog extends Dialog<List<Photo>> {

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

        ImageView infoImage = new ImageView(String.valueOf(getClass().getResource("/com/photos/information-icon.png")));
        infoImage.setFitWidth(25.0);
        infoImage.setFitHeight(25.0);
        infoImage.setPickOnBounds(true);

        Tooltip helpTooltip = Utility.getHelpTooltip("""
                Search all photos based on 2 name-value tags and a date range
                Use AND/OR to combine the next filters, or IGNORE to ignore
                If a tag or date range is left empty, it will be ignored
                Tags are not case sensitive
                If everything is left empty, will show all photos""");
        Tooltip.install(infoImage, helpTooltip);

        setGraphic(infoImage);

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        searchDialogController.tag1_1.setItems(User.getInstance().getTagList());
        searchDialogController.tag2_1.setItems(User.getInstance().getTagList());

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

    private Predicate<Photo> combinePredicate(Predicate<Photo> predicate1, String operator, Predicate<Photo> predicate2) {
        return switch (operator) {
            case "AND" -> predicate1.and(predicate2);
            case "OR" -> predicate1.or(predicate2);
            default -> predicate1; /* Default is to not consider */
        };
    }

    private void updateVisibility() {
        boolean tag1Active = searchDialogController.tag1_1.getValue() != null && !searchDialogController.tag1_2.getText().isBlank();
        boolean tag2Active = searchDialogController.tag2_1.getValue() != null && !searchDialogController.tag2_2.getText().isBlank();
        boolean dateActive = searchDialogController.date1.getValue() != null && searchDialogController.date2.getValue() != null;

        if (tag1Active && tag2Active) {
            searchDialogController.combination1.setVisible(true);
            searchDialogController.combination1.setValue("AND");
        } else {
            searchDialogController.combination1.setVisible(false);
            searchDialogController.combination1.setValue("OR");
        }

        if ((tag1Active || tag2Active) && dateActive) {
            searchDialogController.combination2.setVisible(true);
            searchDialogController.combination2.setValue("AND");
        } else {
            searchDialogController.combination2.setVisible(false);
            searchDialogController.combination2.setValue("OR");
        }
    }
}
