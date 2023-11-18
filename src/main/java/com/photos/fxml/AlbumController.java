package com.photos.fxml;

import com.photos.Photo;
import com.photos.Photos;
import com.photos.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
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
        Photos.getCurrentAlbum().getPhotos().forEach(this::displayPhoto);
        albumLabel.setText("Album: " + Photos.getCurrentAlbum().getName());
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
            } // TODO: Status message that says it couldn't add the photo?
        }
    }

    @Override
    protected ContextMenu getContextMenu(Photo photo, BorderPane borderPane) {
        ContextMenu contextMenu = super.getContextMenu(photo, borderPane);


        return contextMenu;
    }
}
