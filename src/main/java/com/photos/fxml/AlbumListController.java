package com.photos.fxml;

import com.photos.Album;
import com.photos.Photos;
import com.photos.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class AlbumListController implements Initializable {

    @FXML
    private FlowPane albumFlowPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        User.getInstance().getAlbums().forEach(this::displayAlbum);
    }

    private void displayAlbum(Album album) {
        ImageView imageView;
        if (album.getPhotos().isEmpty()) {
            imageView = new ImageView(String.valueOf(getClass().getResource("/com/photos/no-image-icon.png")));
        } else {
            imageView = new ImageView(album.getPhotos().get(album.getPhotos().size() - 1).getPath().toUri().toString());
        }

        imageView.prefHeight(100.0);
        imageView.prefWidth(100.0);
        imageView.setFitHeight(100.0);
        imageView.setFitWidth(100.0);
        imageView.setStyle("-fx-border-color: black;" + "-fx-border-width: 4px;"); // Does not work, need pane wrapper?
        imageView.setOnMouseClicked(mouseEvent -> {
            Photos.setCurrentAlbum(album);
            Photos.switchScene("album.fxml");
        });

        /* Title */
        Label label = new Label(album.getName());
        BorderPane.setAlignment(label, Pos.CENTER);
        BorderPane borderPane = new BorderPane(imageView);
        borderPane.setTop(label);

        /* Context Menu */
        Label ellipsis = new Label("⋮");
        ellipsis.setFont(Font.font(15));
        ellipsis.setStyle("-fx-background-color: #E0E0E0;");
        ellipsis.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
        ContextMenu contextMenu = getContextMenu(album, borderPane);
        ellipsis.setOnMouseClicked(mouseEvent -> contextMenu.show(label, mouseEvent.getScreenX(), mouseEvent.getScreenY()));
        BorderPane.setAlignment(ellipsis, Pos.CENTER_RIGHT);
        borderPane.setBottom(ellipsis);

        albumFlowPane.getChildren().add(borderPane);
    }

    private ContextMenu getContextMenu(Album album, BorderPane borderPane) {
        MenuItem rename = new MenuItem("Rename");
        rename.setOnAction(actionEvent -> {
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setTitle("Add Album");
            inputDialog.setHeaderText("Enter the name of the new album");
            inputDialog.setContentText("Name:");

            inputDialog.showAndWait().ifPresent(album::rename);
        });

        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(actionEvent -> {
            User.getInstance().getAlbums().remove(album);
            albumFlowPane.getChildren().remove(borderPane);
        });

        return new ContextMenu(rename, delete);
    }

    @FXML
    protected void onAddAlbum() {
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle("Add Album");
        inputDialog.setHeaderText("Enter the name of the new album");
        inputDialog.setContentText("Name:");

        inputDialog.showAndWait().ifPresent(s -> {
            Album album = new Album(s);
            User.getInstance().getAlbums().add(album);
            displayAlbum(album);
        });
    }
}
