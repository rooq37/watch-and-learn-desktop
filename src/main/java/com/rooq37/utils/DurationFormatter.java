package com.rooq37.utils;


import javafx.util.Duration;

public class DurationFormatter {

    public static String format(Duration duration) {
        int hours = (int) (duration.toSeconds() / 3600);
        int minutes = ((int) duration.toSeconds() % 3600) / 60;
        int seconds = (int) duration.toSeconds() % 60;
        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        } else if (minutes > 0) {
            return String.format("%02d:%02d", minutes, seconds);
        } else {
            return String.format("0:%02d", seconds);
        }
    }

}
