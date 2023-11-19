package com.photos.fxml;

import com.photos.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;

import java.util.List;

public class SearchResultsController extends PhotosDisplay {

    @FXML
    private Label message;

    public void displayResults(List<Photo> photos) {
        this.photoList = photos;
        photos.forEach(this::displayPhoto);
    }

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
