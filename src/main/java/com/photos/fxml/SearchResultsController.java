package com.photos.fxml;

import com.photos.*;
import com.photos.models.Album;
import com.photos.models.Photo;
import com.photos.models.User;
import com.photos.utility.Utility;
import javafx.fxml.FXML;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;

import java.util.List;

/**
 * Controller of search-results.fxml to show the results of a search.
 */
public class SearchResultsController extends PhotosDisplay {

    /**
     * Initialization setup for the FXML, but since we need context we call this
     * method before fully switching scenes.
     * @param photos The list of photos found as a result of the search
     */
    public void displayResults(List<Photo> photos) {
        this.photoList = photos;
        photos.forEach(this::displayPhoto);
    }

    /**
     * Takes all the photos found and saves it to a new album
     */
    @FXML
    protected void onSaveResults() {
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle("Save Search Results to Album");
        inputDialog.setHeaderText("Enter the name of the new album");
        inputDialog.setContentText("Name:");

        ImageView infoImage = Utility.generateInformationGraphic("""
                Create a new album of all the search results.
                Input its name in the text-field""");
        inputDialog.setGraphic(infoImage);

        inputDialog.showAndWait().ifPresent(s -> {
            Album album = new Album(s);
            if (!User.getInstance().getAlbums().contains(album)) {
                album.getPhotos().addAll(photoList);
                User.getInstance().getAlbums().add(album);

                Photos.setCurrentAlbum(album);
                Photos.switchScene("album.fxml");
            } else {
                Utility.displayErrorMessage(message, "Cannot create album, an album already exists with that name!");
            }
        });
    }
}
