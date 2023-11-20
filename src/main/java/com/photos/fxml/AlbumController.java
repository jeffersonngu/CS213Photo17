package com.photos.fxml;

import com.photos.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;

public class AlbumController extends PhotosDisplay implements Initializable {

    @FXML
    private Label albumLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.photoList = Photos.getCurrentAlbum().getPhotos();
        Photos.getCurrentAlbum().getPhotos().forEach(this::displayPhoto);
        albumLabel.setText("Album: " + Photos.getCurrentAlbum().getName());

        albumLabel.setTooltip(Utility.getHelpTooltip(Photos.getCurrentAlbum().getName()));
    }

    @FXML
    protected void onAddPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.BMP", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(Photos.getMainStage());
        if (selectedFile != null && selectedFile.exists()) {
            Path selectedPath = selectedFile.toPath();

            /* See if it already exists */
            if (Photos.getCurrentAlbum().getPhotos().stream().noneMatch(photo -> photo.getPath().equals(selectedPath))) { /* Is not in current album */
                List<Photo> searchedPhotos = User.getInstance().searchPhotos(photo -> photo.getPath().equals(selectedPath)); /* Is in some other album */
                Photo matchedPhoto = searchedPhotos.isEmpty() ? new Photo(selectedPath) : searchedPhotos.get(0);

                Photos.getCurrentAlbum().getPhotos().add(matchedPhoto);
                displayPhoto(matchedPhoto);
            } else {
                Utility.displayErrorMessage(message, "Photo already exists in this album!");
            }
        }
    }

    @Override
    protected ContextMenu getContextMenu(Photo photo, BorderPane borderPane) {
        ContextMenu contextMenu = super.getContextMenu(photo, borderPane);

        MenuItem movePhoto = new MenuItem("Move");
        movePhoto.setOnAction(actionEvent -> {
            SelectAlbumDialog selectAlbumDialog = new SelectAlbumDialog(photo, "Move to Album",
                    "Pick one album to move this photo to", false);
            selectAlbumDialog.showAndWait().ifPresent(albumList -> {
                Album targetAlbum = albumList.get(0);
                photoList.remove(photo);
                photoFlowPane.getChildren().remove(borderPane);
                targetAlbum.getPhotos().add(photo);
                Utility.displayStatusMessage(message, "Successfully moved photo to album: " + targetAlbum.getName());
            });
        });

        MenuItem removePhoto = new MenuItem("Remove");
        removePhoto.setOnAction(actionEvent -> {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setGraphic(null);
            confirmation.setTitle("Remove Photo Confirmation");
            confirmation.setHeaderText("Are you sure you want to delete this photo?");
            confirmation.setContentText(null);
            confirmation.showAndWait().ifPresent(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    photoList.remove(photo);
                    photoFlowPane.getChildren().remove(borderPane);
                    Utility.displayStatusMessage(message, "Successfully deleted photo!");
                }
            });
        });

        contextMenu.getItems().addAll(movePhoto, removePhoto);
        return contextMenu;
    }
}
