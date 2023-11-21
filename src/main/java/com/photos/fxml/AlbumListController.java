package com.photos.fxml;

import com.photos.*;
import com.photos.models.Album;
import com.photos.models.User;
import com.photos.utility.Utility;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the entire list of albums
 */
public class AlbumListController implements Initializable {

    @FXML
    private Label yourAlbums;

    @FXML
    private FlowPane albumFlowPane;

    @FXML
    private Label message;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        yourAlbums.setText(User.getInstance().getUsername() + "'s albums - " + User.getInstance().getAlbums().size());
        User.getInstance().getAlbums().forEach(this::displayAlbum);
    }

    protected void displayAlbum(Album album) {
        ImageView imageView;
        if (album.getPhotos().isEmpty()) {
            imageView = new ImageView(Photos.getNoImage());
        } else {
            imageView = new ImageView(album.getPhotos().get(album.getPhotos().size() - 1).getPath().toUri().toString());
        }
        BorderPane borderPane = new BorderPane(imageView);
        borderPane.getStyleClass().add("photo-dp");

        Utility.setImageViewDefaultSettings(imageView);
        imageView.setOnMouseClicked(mouseEvent -> {
            Photos.setCurrentAlbum(album);
            Photos.switchScene("album.fxml");
        });

        /* Title */
        Label label = new Label();
        label.textProperty().bind(album.getObservableName().concat("  「" + album.getPhotos().size() + "」"));
        label.setTextOverrun(OverrunStyle.ELLIPSIS);
        label.setAlignment(Pos.CENTER);
        label.setMaxWidth(100.0);

        Tooltip albumNameTooltip = Utility.getHelpTooltip(album.getName());
        albumNameTooltip.textProperty().bind(album.getObservableName());

        label.setTooltip(albumNameTooltip);

        BorderPane.setAlignment(label, Pos.CENTER);
        borderPane.setTop(label);

        /* Bottom */
        Label dateRange = new Label();
        Utility.albumDateRange(album, (photoMinDate, photoMaxDate) -> {
            if (photoMaxDate.equals(photoMinDate)) {
                dateRange.setText(Utility.getDate(photoMinDate.getLastModified()));
            } else {
                dateRange.setText(Utility.getDate(photoMinDate.getLastModified()) + "-"
                        + Utility.getDate(photoMaxDate.getLastModified()));
            }
        });
        BorderPane.setAlignment(dateRange, Pos.CENTER_LEFT);

        /* Context Menu */
        Label ellipsis = Utility.generateEllipsisMenu(getContextMenu(album, borderPane));
        BorderPane.setAlignment(ellipsis, Pos.CENTER_RIGHT);

        BorderPane bottomLabels = new BorderPane();
        bottomLabels.setLeft(dateRange);
        bottomLabels.setRight(ellipsis);
        borderPane.setBottom(bottomLabels);

        albumFlowPane.getChildren().add(borderPane);
    }

    protected ContextMenu getContextMenu(Album album, BorderPane borderPane) {
        MenuItem rename = new MenuItem("Rename");
        rename.setOnAction(actionEvent -> {
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setTitle("Rename Album");
            inputDialog.setHeaderText("Enter the new name of this album");
            inputDialog.setContentText("New Name:");
            inputDialog.getEditor().setText(album.getName());

            ImageView infoImage = Utility.generateInformationGraphic("""
                    Give a new name to this album by inputting
                        it into the text-field.
                    Note that blank or already existing album names are
                        not allowed""");
            inputDialog.setGraphic(infoImage);

            inputDialog.showAndWait().ifPresent(s -> { /* May get weird messages like cannot rename album test to test but that is fine */
                if (!album.rename(s))
                    Utility.displayErrorMessage(message, "Cannot rename album " + album.getName() + " to " + s);
            });
        });

        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(actionEvent -> {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setGraphic(null);
            confirmation.setTitle("Delete Album Confirmation");
            confirmation.setHeaderText("Are you sure you want to delete album: " + album.getName());
            confirmation.setContentText(null);
            confirmation.showAndWait().ifPresent(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    User.getInstance().getAlbums().remove(album);
                    albumFlowPane.getChildren().remove(borderPane);
                    Utility.displayStatusMessage(message, "Successfully deleted album: " + album.getName());
                }
            });
        });

        return new ContextMenu(rename, delete);
    }

    @FXML
    protected void onAddAlbum() {
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle("Add Album");
        inputDialog.setHeaderText("Enter the name of the new album");
        inputDialog.setContentText("Name:");

        ImageView infoImage = Utility.generateInformationGraphic("""
                Give a name to the new empty album by inputting
                it into the text-field
                Note that blank or already existing album names are
                    not allowed""");
        inputDialog.setGraphic(infoImage);

        inputDialog.showAndWait().ifPresent(s -> {
            if (s.isBlank()) {
                Utility.displayErrorMessage(message, "Cannot create album, album name is blank!");
            } else if (!Utility.isUniqueAlbumName(s)) {
                Utility.displayErrorMessage(message, "Cannot create album, an album already exists with that name!");
            } else {
                Album album = new Album(s);
                User.getInstance().getAlbums().add(album);
                Utility.displayStatusMessage(message, "Created album " + album.getName());
                displayAlbum(album);
            }
        });
    }

    @FXML
    protected void onSearch() {
        SearchDialog searchDialog = new SearchDialog();
        searchDialog.showAndWait().ifPresent(results -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("search-results.fxml"));
                Scene searchResultsScene = new Scene(fxmlLoader.load());
                searchResultsScene.getStylesheets().add(Photos.class.getResource("css/search-results.css").toExternalForm());
                Photos.getMainStage().setScene(searchResultsScene);
                ((SearchResultsController) fxmlLoader.getController()).displayResults(results);
                Photos.getMainStage().show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
