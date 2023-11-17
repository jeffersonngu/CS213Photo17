package com.photos.fxml;

import com.photos.Album;
import com.photos.Photos;
import com.photos.User;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AlbumListController implements Initializable {

    @FXML
    private FlowPane albumFlowPane;

    @FXML
    private Label message;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        User.getInstance().getAlbums().forEach(this::displayAlbum);
    }

    protected void displayAlbum(Album album) {
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
        Label label = new Label();
        label.textProperty().bind(album.getObservableName());
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

    protected ContextMenu getContextMenu(Album album, BorderPane borderPane) {
        MenuItem rename = new MenuItem("Rename");
        rename.setOnAction(actionEvent -> {
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setTitle("Rename Album");
            inputDialog.setHeaderText("Enter the new name of this album");
            inputDialog.setContentText("Name:");

            ImageView infoImage = new ImageView(String.valueOf(getClass().getResource("/com/photos/information-icon.png")));
            infoImage.setFitWidth(25.0);
            infoImage.setFitHeight(25.0);
            infoImage.setPickOnBounds(true);

            Tooltip helpTooltip = getHelpTooltip("""
                Give a new name to this album by inputting
                it into the text-field""");

            Tooltip.install(infoImage, helpTooltip);
            inputDialog.setGraphic(infoImage);

            inputDialog.showAndWait().ifPresent(album::rename);
        });

        MenuItem delete = new MenuItem("Delete");
        delete.setOnAction(actionEvent -> {
            // TODO: Confirmation window
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

        ImageView infoImage = new ImageView(String.valueOf(getClass().getResource("/com/photos/information-icon.png")));
        infoImage.setFitWidth(25.0);
        infoImage.setFitHeight(25.0);
        infoImage.setPickOnBounds(true);

        Tooltip helpTooltip = getHelpTooltip("""
                Give a name to the new empty album by inputting
                it into the text-field""");

        Tooltip.install(infoImage, helpTooltip);
        inputDialog.setGraphic(infoImage);

        inputDialog.showAndWait().ifPresent(s -> {
            Album album = new Album(s);
            if (!User.getInstance().getAlbums().contains(album)) {
                User.getInstance().getAlbums().add(album);
                displayAlbum(album);
            } else { /* Not working */
                message.setTextFill(Color.color(1.0, 0.0, 0.0));
                message.setText("Cannot create album, an album already exists with that name!");
                PauseTransition pause = new PauseTransition(Duration.seconds(5));
                pause.setOnFinished(e -> message.setText(null));
                pause.play();
            }
        });
    }

    private static Tooltip getHelpTooltip(String tooltip) {
        Tooltip helpTooltip = new Tooltip(tooltip);
        helpTooltip.setShowDelay(Duration.ZERO);
        helpTooltip.setShowDuration(Duration.INDEFINITE);
        helpTooltip.setWrapText(true);
        helpTooltip.setStyle("-fx-font-size: 16;");
        return helpTooltip;
    }

    @FXML
    protected void onSearch() {
        SearchDialog searchDialog = new SearchDialog();
        searchDialog.showAndWait().ifPresent(results -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("search-results.fxml"));
                Photos.getMainStage().setScene(new Scene(fxmlLoader.load()));
                ((SearchResultsController) fxmlLoader.getController()).displayResults(results);
                Photos.getMainStage().show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
