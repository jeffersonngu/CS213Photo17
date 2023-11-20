package com.photos.fxml;

import com.photos.models.Photo;
import com.photos.models.User;
import com.photos.utility.Utility;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;

@Deprecated
public class AddTagDialog extends Dialog<Void> {

    private final AddTagDialogController addTagDialogController;

    public AddTagDialog(Photo photo) {
        super();

        /* Load the fxml file */
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-tag-dialog.fxml"));
        try {
            setDialogPane(fxmlLoader.load());
            this.addTagDialogController = fxmlLoader.getController();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        /* Load extras */
        setTitle("Add tag");
        setHeaderText("Tag the image using an existing tag, or create a new tag.");

        ImageView infoImage = new ImageView(String.valueOf(getClass().getResource("/com/photos/information-icon.png")));
        infoImage.setFitWidth(25.0);
        infoImage.setFitHeight(25.0);
        infoImage.setPickOnBounds(true);

        Tooltip helpTooltip = Utility.getHelpTooltip("""
                Add a new name-value tag for the selected Photo
                Note, if the name of the tag was not listed,
                    it will generate a new type of tag permanently
                Tags will allow spaces and are case-sensitive
                    (i.e. different capitalization results in different values)""");
        Tooltip.install(infoImage, helpTooltip);

        setGraphic(infoImage);

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        addTagDialogController.tag1.setItems(FXCollections.observableArrayList(User.getInstance().getTagCollection()));
        addTagDialogController.tag1.setEditable(true);
        addTagDialogController.tag1.valueProperty().addListener((observable, oldValue, newValue) -> listenTag());

        /* Set results */
        setResultConverter(dialogButton -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            if (data == ButtonBar.ButtonData.OK_DONE) {
                String tag1 = addTagDialogController.tag1.getValue();
                String tag2 = addTagDialogController.tag2.getText();
                if (tag1 != null && !tag1.isBlank() && !tag2.isBlank()) {
                    photo.addTag(tag1, tag2);
                    if (!User.getInstance().getTagCollection().contains(tag1)) {
                        User.getInstance().getTagMap().put(tag1, true);
                    }
                }
            }
            return null;
        });
    }

    private void listenTag() {
        String value = addTagDialogController.tag1.getValue();
        if (value == null) return;
    }
}
