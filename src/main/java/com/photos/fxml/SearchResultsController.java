package com.photos.fxml;

import com.photos.Album;
import com.photos.Photo;
import com.photos.Photos;
import com.photos.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.List;

public class SearchResultsController extends PhotosDisplay {

    private List<Photo> searchResults;

    public void displayResults(List<Photo> photos) {
        this.searchResults = photos;
        photos.forEach(this::displayPhoto);
    }

    @FXML
    protected void onSaveResults(ActionEvent actionEvent) {
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle("Save Search Results to Album");
        inputDialog.setHeaderText("Enter the name of the new album");
        inputDialog.setContentText("Name:");

        ImageView infoImage = new ImageView(String.valueOf(getClass().getResource("/com/photos/information-icon.png")));
        infoImage.setFitWidth(25.0);
        infoImage.setFitHeight(25.0);
        infoImage.setPickOnBounds(true);

        Tooltip helpTooltip = getHelpTooltip();

        Tooltip.install(infoImage, helpTooltip);
        inputDialog.setGraphic(infoImage);

        inputDialog.showAndWait().ifPresent(s -> {
            Album album = new Album(s);
            album.getPhotos().addAll(searchResults);
            User.getInstance().getAlbums().add(album);

            Photos.setCurrentAlbum(album);
            Photos.switchScene("album.fxml");
        });
    }

    private static Tooltip getHelpTooltip() {
        Tooltip helpTooltip = new Tooltip("""
                Create a new album of all the search results.
                Input its name in the text-field""");
        helpTooltip.setShowDelay(Duration.ZERO);
        helpTooltip.setShowDuration(Duration.INDEFINITE);
        helpTooltip.setWrapText(true);
        helpTooltip.setStyle("-fx-font-size: 16;");
        return helpTooltip;
    }
}
