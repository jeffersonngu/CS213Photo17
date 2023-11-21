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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Controller of image-slide-show.fxml to show a slideshow
 * of images from a list.
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

    /**
     * List of photos to swap between.
     */
    protected List<Photo> photoList;

    /**
     * Note which photo is currently viewed via an index of {@link #photoList}.
     */
    protected int currentIndex;

    /**
     * The current photo being displayed
     */
    protected Photo currentPhoto;

    /**
     * Initialization setup for the FXML, but since we need context we call this
     * method before fully switching scenes.
     * @param photoList The photos to consider displaying.
     * @param initialPhoto The first photo to show.
     */
    public void setupImageSlideShow(List<Photo> photoList, Photo initialPhoto) {
        if (!photoList.contains(initialPhoto)) throw new RuntimeException("Initial photo is not in the list!");
        this.photoList = photoList;
        this.currentIndex = photoList.indexOf(initialPhoto);
        this.currentPhoto = initialPhoto;

        slideshowImage.setCache(true);

        updateImage();
    }

    /**
     * Updates the current view to show the new image
     */
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

    /**
     * Attempt to move back one photo in the list. If cannot then do nothing.
     */
    @FXML
    protected void onBackButton() {
        if (currentIndex <= 0) return;
        currentPhoto = photoList.get(--currentIndex);
        updateImage();
    }

    /**
     * Attempt to move forward one photo in the list. If cannot then do nothing.
     */
    @FXML
    protected void onForwardButton() {
        if (currentIndex >= (photoList.size() - 1)) return;
        currentPhoto = photoList.get(++currentIndex);
        updateImage();
    }

    /**
     * Closes the window.
     */
    @FXML
    protected void onBackToAlbumButton() {
        Stage currentStage = (Stage) backButton.getScene().getWindow();
        currentStage.close();
    }

    /**
     * For convenience, allow moving backward and forward using arrow keys.
     */
    @FXML
    protected void onKeyReleased(KeyEvent event) {
        if (event.getCode().equals(KeyCode.RIGHT)) {
            if (currentIndex >= (photoList.size() - 1)) return;
            currentPhoto = photoList.get(++currentIndex);
            updateImage();
        } else if (event.getCode().equals(KeyCode.LEFT)) {
            if (currentIndex <= 0) return;
            currentPhoto = photoList.get(--currentIndex);
            updateImage();
        }
    }
}
