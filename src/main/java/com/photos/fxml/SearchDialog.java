package com.photos.fxml;

import com.photos.Photo;
import com.photos.User;
import com.photos.Utility;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

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
                Use AND/OR to combine the next filters, or SKIP to ignore
                If a tag or date range is left empty, it will be ignored
                Tags are not case sensitive
                If everything is left empty, will show all photos""");
        Tooltip.install(infoImage, helpTooltip);

        setGraphic(infoImage);

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        searchDialogController.tag1_1.setItems(User.getInstance().getTagList());
        searchDialogController.tag2_1.setItems(User.getInstance().getTagList());

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

        if (!searchDialogController.tag1_1.getValue().isEmpty() && !searchDialogController.tag1_2.getText().isEmpty()) {
            result = combinePredicate(result, "AND",
                    photo -> photo.hasTag(searchDialogController.tag1_1.getValue(), searchDialogController.tag1_2.getText()));
        }

        if (!searchDialogController.tag2_1.getValue().isEmpty() && !searchDialogController.tag2_2.getText().isEmpty()) {
            result = combinePredicate(result, (String) searchDialogController.combination1.getValue(),
                    photo -> photo.hasTag(searchDialogController.tag2_1.getValue(), searchDialogController.tag2_2.getText()));
        }

        if (searchDialogController.date1.getValue() != null && searchDialogController.date2.getValue() != null) {
            result = combinePredicate(result, (String) searchDialogController.combination2.getValue(),
                    photo -> photo.getLastModified().to(TimeUnit.DAYS) >= searchDialogController.date1.getValue().toEpochDay()
                            && photo.getLastModified().to(TimeUnit.DAYS) <= searchDialogController.date2.getValue().toEpochDay());
        }

        // TODO: Set it up so that the choice boxes only show up on two inputs or more + only behind the 2nd/3rd one otherwise setting to OR breaks it

        return result;
    }

    private Predicate<Photo> combinePredicate(Predicate<Photo> predicate1, String operator, Predicate<Photo> predicate2) {
        return switch (operator) {
            case "AND" -> predicate1.and(predicate2);
            case "OR" -> predicate1.or(predicate2);
            default -> predicate1; /* Default is "SKIP" */
        };
    }
}
