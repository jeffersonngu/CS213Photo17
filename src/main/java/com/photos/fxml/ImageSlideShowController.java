package com.photos.fxml;

import com.photos.Photos;
import com.photos.models.Photo;
import com.photos.utility.Utility;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Controller for each image slideshow
 */
public class ImageSlideShowController {

    @FXML
    protected ImageView slideshowImage;

    @FXML
    protected Label slideshowCaption;

    @FXML
    protected Label slideshowDateTime;

    @FXML
    protected ListView<String> slideshowTags;

    @FXML
    protected Button backButton;

    protected List<Photo> photoList;
    protected int currentIndex;

    protected Photo currentPhoto;

    public void setupImageSlideShow(List<Photo> photoList, Photo initialPhoto) {
        if (!photoList.contains(initialPhoto)) throw new RuntimeException("Initial photo is not in the list!");
        this.photoList = photoList;
        this.currentIndex = photoList.indexOf(initialPhoto);
        this.currentPhoto = initialPhoto;

        slideshowImage.setCache(true);

        updateImage();
    }

    protected void updateImage() {
        Image image = new Image(currentPhoto.getPath().toUri().toString());
        if (image.getException() instanceof FileNotFoundException) {
            image = Photos.getNoImage();
            slideshowCaption.textProperty().unbind();
            slideshowCaption.setText("Missing Image: " + currentPhoto.getPath().toUri());
            slideshowDateTime.setText("");
            slideshowTags.setItems(null);
        } else {
            slideshowCaption.textProperty().bind(currentPhoto.getObservableCaption());
            slideshowCaption.setTooltip(currentPhoto.getCaption().isBlank() ? null :
                    Utility.getHelpTooltip(currentPhoto.getCaption()));
            slideshowDateTime.setText(Utility.getDateTime(currentPhoto.getLastModified()));
            slideshowTags.setItems(FXCollections.observableArrayList(currentPhoto.getTags()));
        }
        slideshowImage.setImage(image);
    }

    @FXML
    protected void onBackButton() {
        if (currentIndex <= 0) return;
        currentPhoto = photoList.get(--currentIndex);
        updateImage();
    }

    @FXML
    protected void onForwardButton() {
        if (currentIndex >= (photoList.size() - 1)) return;
        currentPhoto = photoList.get(++currentIndex);
        updateImage();
    }

    @FXML
    protected void onBackToAlbumButton() {
        Stage currentStage = (Stage) backButton.getScene().getWindow();
        currentStage.close();
    }
}
