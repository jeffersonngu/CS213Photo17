package com.photos.fxml;

import com.photos.Album;
import com.photos.Photo;
import com.photos.Photos;
import com.photos.User;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.List;

public class SearchResultsController extends PhotosDisplay {

    @FXML
    private Label message;

    private List<Photo> searchResults;

    public void displayResults(List<Photo> photos) {
        this.searchResults = photos;
        photos.forEach(this::displayPhoto);
    }

    @FXML
    protected void onSaveResults() {
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
            if (!User.getInstance().getAlbums().contains(album)) {
                User.getInstance().getAlbums().add(album);
                album.getPhotos().addAll(searchResults);
                User.getInstance().getAlbums().add(album);

                Photos.setCurrentAlbum(album);
                Photos.switchScene("album.fxml");
            } else { /* Not working */
                message.setTextFill(Color.color(1.0, 0.0, 0.0));
                message.setText("Cannot create album, an album already exists with that name!");
                PauseTransition pause = new PauseTransition(Duration.seconds(5));
                pause.setOnFinished(e -> message.setText(null));
                pause.play();
            }
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
