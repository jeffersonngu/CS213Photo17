package com.photos.fxml;

import com.photos.Photo;
import com.photos.Photos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class PhotosDisplay {

    @FXML
    protected FlowPane photoFlowPane;

    protected void displayPhoto(Photo photo) {
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

    protected ContextMenu getContextMenu(Photo photo, BorderPane borderPane) {
        // TODO: Add MenuItems
        MenuItem addTag = new MenuItem("Add Tag");
        addTag.setOnAction(actionEvent -> {

        });
        return new ContextMenu(addTag);
    }

    @FXML
    protected void onReturnToAlbums(ActionEvent actionEvent) {
        Photos.switchScene("album-list.fxml");
    }
}
