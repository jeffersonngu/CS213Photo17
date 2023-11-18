package com.photos;

import javafx.scene.control.Tooltip;
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
}
