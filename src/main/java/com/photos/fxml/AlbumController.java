package com.photos.fxml;

import com.photos.Photo;
import com.photos.Photos;
import com.photos.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;

public class AlbumController implements Initializable {

    @FXML
    private FlowPane photoFlowPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Photos.getCurrentAlbum().getPhotos().forEach(this::displayPhoto);
    }

    private void displayPhoto(Photo photo) {
        ImageView imageView = new ImageView(photo.getPath().toUri().toString());
        imageView.prefHeight(100.0);
        imageView.prefWidth(100.0);
        imageView.setFitHeight(100.0);
        imageView.setFitWidth(100.0);
        imageView.setStyle("-fx-border-color: black;" + "-fx-border-width: 4px;"); // Does not work, need pane wrapper?
        imageView.setOnMouseClicked(mouseEvent -> {
            // TODO: Open image
        });

        /* Title */
        Label label = new Label(photo.getCaption());
        BorderPane.setAlignment(label, Pos.CENTER);
        BorderPane borderPane = new BorderPane(imageView);
        borderPane.setTop(label);

        /* Context Menu */
        Label ellipsis = new Label("⋮");
        ellipsis.setFont(Font.font(15));
        ellipsis.setStyle("-fx-background-color: #E0E0E0;");
        ellipsis.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
        ContextMenu contextMenu = getContextMenu(photo, borderPane);
        ellipsis.setOnMouseClicked(mouseEvent -> contextMenu.show(label, mouseEvent.getScreenX(), mouseEvent.getScreenY()));
        BorderPane.setAlignment(ellipsis, Pos.CENTER_RIGHT);
        borderPane.setBottom(ellipsis);

        photoFlowPane.getChildren().add(borderPane);
    }

    private ContextMenu getContextMenu(Photo photo, BorderPane borderPane) {
        // TODO: Add MenuItems
        return new ContextMenu();
    }

    @FXML
    public void onAddPhoto(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.BMP", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(Photos.getMainStage());
        if (selectedFile != null && selectedFile.exists()) {
            Path selectedPath = selectedFile.toPath();
            List<Photo> searchedPhotos = User.getInstance().searchPhotos(photo -> selectedPath.equals(photo.getPath()));
            Photo photo = searchedPhotos.isEmpty() ? new Photo(selectedPath) : searchedPhotos.get(0);
            Photos.getCurrentAlbum().getPhotos().add(photo);
            displayPhoto(photo);
        }

    }

    public void onReturnToAlbums(ActionEvent actionEvent) {
        Photos.switchScene("album-list.fxml");
    }
}
