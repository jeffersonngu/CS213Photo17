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

public class AddTagDialog extends Dialog<Void> {

    public AddTagDialog(Photo photo) {
        super();

        /* Load the fxml file */
        AddTagDialogController addTagDialogController;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-tag-dialog.fxml"));
        try {
            setDialogPane(fxmlLoader.load());
            addTagDialogController = fxmlLoader.getController();
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
                Add a new name-value tag for the selected Photo
                Note, if the name of the tag was not listed
                It will generate a new type of tag permanently"""); // TODO: Not permanently
        Tooltip.install(infoImage, helpTooltip);

        setGraphic(infoImage);

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        addTagDialogController.tag1.setItems(User.getInstance().getTagList());
        addTagDialogController.tag1.setEditable(true);

        /* Set results */
        setResultConverter(dialogButton -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            if (data == ButtonBar.ButtonData.OK_DONE
                    && !addTagDialogController.tag1.getValue().isBlank()
                    && !addTagDialogController.tag2.getText().isBlank()) {
                photo.addTag(addTagDialogController.tag1.getValue(), addTagDialogController.tag2.getText());
                User.getInstance().getTagList().add(addTagDialogController.tag1.getValue()); /* Only add the new tag if we have a valid tag */
            }
            return null;
        });
    }
}
