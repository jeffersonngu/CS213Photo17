package com.photos;

import javafx.animation.PauseTransition;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.nio.file.attribute.FileTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.function.BiConsumer;

/**
 * A collection of non-class specific pure methods to promote code reuse
 */
public class Utility {

    public static Tooltip getHelpTooltip(String tooltip) {
        Tooltip helpTooltip = new Tooltip(tooltip);
        helpTooltip.setShowDelay(Duration.ZERO);
        helpTooltip.setShowDuration(Duration.INDEFINITE);
        helpTooltip.setWrapText(true);
        helpTooltip.setStyle("-fx-font-size: 16;");
        return helpTooltip;
    }

    public static ImageView generateInformationGraphic(String tooltip) {
        ImageView infoImage = new ImageView(String.valueOf(Utility.class.getResource("/com/photos/information-icon.png")));
        infoImage.setFitWidth(25.0);
        infoImage.setFitHeight(25.0);
        infoImage.setPickOnBounds(true);

        Tooltip helpTooltip = Utility.getHelpTooltip(tooltip);

        Tooltip.install(infoImage, helpTooltip);

        return infoImage;
    }

    public static String getDate(FileTime fileTime) {
        return fileTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }

    public static String getDateTime(FileTime fileTime) {
        return fileTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("MMMM d, yyyy h:mm:ssa"));
    }

    /**
     * Takes in an empty label and displays a colored message for a few seconds
     * @param message Empty label
     * @param text The message
     * @param color The color of the message
     */
    public static void displayMessage(Label message, String text, Paint color) {
        message.setTextFill(color);
        message.setText(text);
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(e -> {
            if (message.getText().equals(text)) message.setText(null);
        });
        pause.play();
    }

    /**
     * Takes in an empty label and displays an error message in red for a few seconds
     * @param message Empty label
     * @param text The message
     */
    public static void displayErrorMessage(Label message, String text) {
        displayMessage(message, text, Color.color(1.0, 42.0/255.0, 4.0/255.0)); /* https://www.astrouxds.com/patterns/status-system/ */
    }

    /**
     * Takes in an empty label and displays a status message in green for a few seconds
     * @param message Empty label
     * @param text The message
     */
    public static void displayStatusMessage(Label message, String text) {
        displayMessage(message, text, Color.color(0.0, 226.0/255.0, 0.0));
    }

    public static void albumDateRange(Album album, BiConsumer<Photo, Photo> action) {
        album.getPhotos().stream()
                .min(Comparator.comparing(Photo::getLastModified))
                .ifPresent(photoMinDate -> album.getPhotos().stream()
                        .max(Comparator.comparing(Photo::getLastModified))
                        .ifPresent(photoMaxDate -> action.accept(photoMinDate, photoMaxDate)));
    }

    public static void setImageViewDefaultSettings(ImageView imageView) {
        imageView.prefHeight(100.0);
        imageView.prefWidth(100.0);
        imageView.setFitHeight(100.0);
        imageView.setFitWidth(100.0);
        // imageView.setStyle("-fx-border-color: black;" + "-fx-border-width: 4px;"); // Does not work, need pane wrapper?
        // TODO: Rounded borders
        imageView.setPickOnBounds(true);

        if (imageView.getImage().getException() instanceof FileNotFoundException) {
            imageView.setImage(Photos.getNoImage());
        }
    }

    public static Label generateEllipsisMenu(ContextMenu contextMenu) {
        Label ellipsis = new Label("⋮");
        ellipsis.setFont(Font.font(15));
        ellipsis.setStyle("-fx-background-color: #E0E0E0;");
        ellipsis.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
        ellipsis.setOnMouseClicked(mouseEvent -> contextMenu.show(ellipsis, mouseEvent.getScreenX(), mouseEvent.getScreenY()));

        return ellipsis;
    }

    /**
     * Checks if the given album name is unique amongst other album names.
     * @param name The album name
     * @return Whether the new album name is unique
     */
    public static boolean isUniqueAlbumName(String name) {
        return User.getInstance().getAlbums().stream()
                .noneMatch(album -> album.getName().equalsIgnoreCase(name));
    }

    /**
     * Checks if the given album name is unique except in the case it happens to be the old name as well.
     * This is useful for renaming operations.
     * @param oldName The previous name
     * @param newName The new name
     * @return Whether the new album name is unique
     */
    public static boolean isUniqueAlbumName(String oldName, String newName) {
        return User.getInstance().getAlbums().stream()
                .filter(album -> !album.getName().equalsIgnoreCase(oldName))
                .noneMatch(album -> album.getName().equalsIgnoreCase(newName));
    }
}
