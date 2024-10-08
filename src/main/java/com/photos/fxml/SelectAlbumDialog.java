package com.photos.fxml;

import com.photos.models.Album;
import com.photos.models.Photo;
import com.photos.models.User;
import com.photos.utility.Utility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.util.List;

/**
 * Dialog that is shown when a user wants to move/copy
 * photos around.
 */
public class SelectAlbumDialog extends Dialog<List<Album>> {

    /**
     * Instance of the controller to interface with the fxml
     */
    private final SelectAlbumDialogController selectAlbumDialogController;

    /**
     * A list of all selected {@link Album}s, if {@link #hasMultiSelect}
     * is false then will only allow a size of 1 at most
     */
    private final ObservableList<Album> selectedAlbums;

    /**
     * Whether we allow multi-selection of albums or not
     */
    private final boolean hasMultiSelect;

    /**
     * Allows modification of a previous selection
     * Only useful when {@link #hasMultiSelect} is false
     */
    private BorderPane previousBorderPane = null;

    /**
     * Initializes all parameters, is flexible to allow
     * different operations such as move/copy of {@link Photo}s
     * on {@link Album}s
     * @param self The photo being operated on
     * @param title The title of the dialog
     * @param header The header of the dialog
     * @param hasMultiSelect Whether we allow multi-selection of {@link Album}s
     */
    public SelectAlbumDialog(Photo self, String title, String header, boolean hasMultiSelect) {
        super();

        this.selectedAlbums = FXCollections.observableArrayList();
        this.hasMultiSelect = hasMultiSelect;

        /* Load the fxml file */
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("select-album-dialog.fxml"));
        try {
            setDialogPane(fxmlLoader.load());
            this.selectAlbumDialogController = fxmlLoader.getController();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        /* Load extras */
        setTitle(title);
        setHeaderText(header);
        setGraphic(null);

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        User.getInstance().getAlbums().stream()
                .filter(album -> !album.getPhotos().contains(self))
                .forEach(this::displayAlbum);

        /* Set results */
        setResultConverter(dialogButton -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            if (data == ButtonBar.ButtonData.OK_DONE && !selectedAlbums.isEmpty()) {
                return selectedAlbums;
            }
            return null;
        });
    }

    /**
     * Displays the album, including its thumbnail, text information, and events
     * @param album The album to display
     */
    protected void displayAlbum(Album album) {
        ImageView imageView;
        if (album.getPhotos().isEmpty()) {
            imageView = new ImageView(String.valueOf(getClass().getResource("/com/photos/no-image-icon.png")));
        } else {
            imageView = new ImageView(album.getPhotos().get(album.getPhotos().size() - 1).getPath().toUri().toString());
        }
        BorderPane borderPane = new BorderPane(imageView);
        borderPane.setPickOnBounds(true);
        borderPane.setOnMouseClicked(mouseEvent -> updateSelection(borderPane, album));

        Utility.setImageViewDefaultSettings(imageView);

        Label label = new Label();
        label.textProperty().bind(album.getObservableName());
        label.setTextOverrun(OverrunStyle.ELLIPSIS);
        label.setAlignment(Pos.CENTER);
        label.setMaxWidth(100.0);
        BorderPane.setAlignment(label, Pos.CENTER);

        Label albumSize = new Label("「" + album.getPhotos().size() + "」");
        BorderPane.setAlignment(albumSize, Pos.CENTER);

        borderPane.setTop(label);
        borderPane.setBottom(albumSize);

        selectAlbumDialogController.albumFlowPane.getChildren().add(borderPane);
    }

    /**
     * When an album is selected, either select it or deselect it.
     * When {@link #hasMultiSelect} is false, will also deselect
     * all other albums
     * @param borderPane The borderPane of the currently clicked album
     * @param album The album that has been clicked
     */
    protected void updateSelection(BorderPane borderPane, Album album) {
        if (selectedAlbums.contains(album)) {
            borderPane.setStyle("");
            selectedAlbums.remove(album);
        } else {
            if (!hasMultiSelect) {
                if (previousBorderPane != null) previousBorderPane.setStyle("");
                selectedAlbums.clear();
                previousBorderPane = borderPane;
            }
            borderPane.setStyle("-fx-border-color: black; -fx-border-width: 1px;");
            selectedAlbums.add(album);
        }
    }
}
