package com.photos;

import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

import java.nio.file.attribute.FileTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Utility {

    public static Tooltip getHelpTooltip(String tooltip) {
        Tooltip helpTooltip = new Tooltip(tooltip);
        helpTooltip.setShowDelay(Duration.ZERO);
        helpTooltip.setShowDuration(Duration.INDEFINITE);
        helpTooltip.setWrapText(true);
        helpTooltip.setStyle("-fx-font-size: 16;");
        return helpTooltip;
    }

    public static String getDate(FileTime fileTime) {
        return fileTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
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
        pause.setOnFinished(e -> message.setText(null));
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
}
