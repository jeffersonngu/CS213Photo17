package com.photos.fxml;

import com.photos.Album;
import com.photos.User;
import javafx.event.ActionEvent;
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
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class AlbumListController implements Initializable {

    @FXML
    private FlowPane albumFlowPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        User.getInstance().getAlbums().forEach(this::displayAlbum);

        displayAlbum(new Album("Empty Test"));
    }

    private void displayAlbum(Album album) {
        ImageView imageView = album.getPhotos().isEmpty() ? new ImageView(String.valueOf(getClass().getResource("/com/photos/missing-image.png"))) : new ImageView();
        imageView.prefHeight(100.0);
        imageView.prefWidth(100.0);
        imageView.setFitHeight(100.0);
        imageView.setFitWidth(100.0);
        // imageView.setStyle("-fx-border-color: black; -fx-border-width: 2px;"); // Does not work, need pane wrapper?

        /* Title */
        Label label = new Label(album.getName());
        BorderPane.setAlignment(label, Pos.CENTER);
        BorderPane borderPane = new BorderPane(imageView);
        borderPane.setTop(label);

        /* Context Menu */
        Label ellipsis = new Label("⋮");
        MenuItem rename = new MenuItem("Rename");
        rename.setOnAction(actionEvent -> {
            System.out.println("Rename" + album.getName());
        });

        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(actionEvent -> {
            System.out.println("Delete " + album.getName());
        });

        ContextMenu contextMenu = new ContextMenu(rename, delete);
        ellipsis.setFont(Font.font(15));
        ellipsis.setStyle("-fx-background-color: #E0E0E0;");
        ellipsis.setOnMouseClicked(mouseEvent -> contextMenu.show(label, mouseEvent.getScreenX(), mouseEvent.getScreenY()));
        ellipsis.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
        BorderPane.setAlignment(ellipsis, Pos.CENTER_RIGHT);
        borderPane.setBottom(ellipsis);
        albumFlowPane.getChildren().add(borderPane);
    }

    @FXML
    protected void onAddAlbum(ActionEvent actionEvent) {
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle("Add Album");
        inputDialog.setHeaderText("Enter the name of the new album");
        inputDialog.setContentText("Name:");

        inputDialog.showAndWait().ifPresent(s -> {
            Album album = new Album(s); // TODO: Add this to the actual albumList
            System.out.println("Input: " + s);
        });
    }
}
