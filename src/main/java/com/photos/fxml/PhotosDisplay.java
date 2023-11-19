package com.photos.fxml;

import com.photos.Photo;
import com.photos.Photos;
import com.photos.User;
import com.photos.Utility;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * Base class for displaying Photos
 * @apiNote Inheritors of this class should have a
 *  {@see PhotosDisplay} and initialize {@link #photoList} for the code to function
 */
public abstract class PhotosDisplay {

    @FXML
    protected FlowPane photoFlowPane;

    @FXML
    protected Label message;

    protected List<Photo> photoList;

    protected void displayPhoto(Photo photo) {
        ImageView imageView = new ImageView(photo.getPath().toUri().toString());
        Utility.setImageViewDefaultSettings(imageView);
        imageView.setOnMouseClicked(mouseEvent -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("image-slideshow.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Photo Slideshow");
                stage.setScene(new Scene(fxmlLoader.load(), 600, 400));
                ((ImageSlideShowController) fxmlLoader.getController()).setupImageSlideShow(photoList, photo);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        /* Title */
        Label label = new Label();
        label.textProperty().bind(photo.getObservableCaption());
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

    protected ContextMenu getContextMenu(Photo photo, BorderPane borderPane) {
        MenuItem setCaption = new MenuItem("Set Caption");
        setCaption.setOnAction(actionEvent -> {
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setTitle("Caption Photo");
            inputDialog.setHeaderText("Give a caption for the photo");
            inputDialog.setContentText("Caption:");
            inputDialog.getEditor().setText(photo.getCaption());
            
            inputDialog.setGraphic(null);

            inputDialog.showAndWait().ifPresent(photo::setCaption);
        });

        MenuItem addTag = new MenuItem("Add Tag");
        addTag.setOnAction(actionEvent -> {
            AddTagDialog addTagDialog = new AddTagDialog(photo);
            addTagDialog.showAndWait();
        });

        MenuItem copyToAlbum = new MenuItem("Copy to Album");
        copyToAlbum.setOnAction(actionEvent -> {
            SelectAlbumDialog selectAlbumDialog = new SelectAlbumDialog(photo, "Copy to Album",
                    "Pick one or more albums to copy the picture to", true);
            selectAlbumDialog.showAndWait().ifPresent(albumList -> albumList.forEach(album -> album.getPhotos().add(photo)));
        });

        return new ContextMenu(setCaption, addTag, copyToAlbum);
    }

    @FXML
    protected void onReturnToAlbums() {
        Photos.switchScene("album-list.fxml");
    }
}
