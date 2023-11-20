package com.photos.fxml;

import com.photos.Photos;
import com.photos.models.Photo;
import com.photos.utility.Utility;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * Base class for displaying Photos.
 * Inheritors of this controller should have a {@link #photoFlowPane} and {@link #message}. Additionally, they need to initialize {@link #photoList}
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
                stage.setScene(new Scene(fxmlLoader.load()));
                stage.initModality(Modality.APPLICATION_MODAL);

                ((ImageSlideShowController) fxmlLoader.getController()).setupImageSlideShow(photoList, photo);
                stage.showAndWait();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        /* Title */
        Label label = new Label();
        label.setTextOverrun(OverrunStyle.ELLIPSIS);
        label.setAlignment(Pos.CENTER);
        label.setMaxWidth(100.0);
        label.textProperty().bind(photo.getObservableCaption());
        BorderPane.setAlignment(label, Pos.CENTER);
        BorderPane borderPane = new BorderPane(imageView);
        borderPane.setTop(label);

        Tooltip captionTooltip = Utility.getHelpTooltip(photo.getCaption());
        captionTooltip.textProperty().bind(photo.getObservableCaption());

        label.setTooltip(captionTooltip);

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

        MenuItem editTags = new MenuItem("Edit Tags");
        editTags.setOnAction(actionEvent -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("edit-tags.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Edit Tags");
                stage.setScene(new Scene(fxmlLoader.load()));
                stage.initModality(Modality.APPLICATION_MODAL);

                ((EditTagsController) fxmlLoader.getController()).setupEditTags(photo);
                stage.showAndWait();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        MenuItem copyToAlbum = new MenuItem("Copy to Album");
        copyToAlbum.setOnAction(actionEvent -> {
            SelectAlbumDialog selectAlbumDialog = new SelectAlbumDialog(photo, "Copy to Album",
                    "Pick one or more albums to copy the picture to", true);
            selectAlbumDialog.showAndWait().ifPresent(albumList -> albumList.forEach(album -> album.getPhotos().add(photo)));
        });

        return new ContextMenu(setCaption, editTags, copyToAlbum);
    }

    @FXML
    protected void onReturnToAlbums() {
        Photos.switchScene("album-list.fxml");
    }
}
