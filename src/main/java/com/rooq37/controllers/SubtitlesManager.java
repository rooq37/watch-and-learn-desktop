package com.rooq37.controllers;

import com.rooq37.model.SubtitleEntity;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.List;

import static com.rooq37.controllers.SubtitlesManager.Mode.*;

public class SubtitlesManager {

    private final List<SubtitleEntity> subtitles;
    private SubtitleEntity currentSubtitle;
    private int currentIndex;
    private String upperSubtitleMode = NONE;
    private String lowerSubtitleMode = NONE;

    public SubtitlesManager(List<SubtitleEntity> subtitles) {
        this.subtitles = subtitles;
        this.currentIndex = 0;
    }

    public void proceed(Duration currentTime) {
        SubtitleEntity temp = subtitles.get(currentIndex);
        if (currentTime.greaterThanOrEqualTo(temp.getStart()) && currentTime.lessThanOrEqualTo(temp.getStop())) {
            currentSubtitle = temp;
        } else if (currentTime.greaterThan(temp.getStop())) {
            currentSubtitle = null;
            currentIndex = Math.min(currentIndex + 1, subtitles.size() - 1);
            temp = subtitles.get(currentIndex);
            if (currentTime.greaterThanOrEqualTo(temp.getStart()) && currentTime.lessThanOrEqualTo(temp.getStop())) {
                currentSubtitle = temp;
            }
        }
    }

    public void seek(Duration targetTime) {
        this.currentIndex = findCurrentIndex(targetTime);
        this.proceed(targetTime);
    }

    public Duration jumpToPreviousSubtitle() {
        currentIndex = Math.max(0, currentIndex - 1);
        SubtitleEntity subtitleEntity = subtitles.get(currentIndex);
        this.proceed(subtitleEntity.getStart());
        return subtitleEntity.getStart();
    }

    public Duration jumpToNextSubtitle() {
        currentIndex = Math.min(currentIndex + 1, subtitles.size() - 1);
        SubtitleEntity subtitleEntity = subtitles.get(currentIndex);
        this.proceed(subtitleEntity.getStart());
        return subtitleEntity.getStart();
    }

    private int findCurrentIndex(Duration targetTime) {
        for (int i = 0; i < subtitles.size(); i++) {
            SubtitleEntity current = subtitles.get(i);
            if (current.getStop().greaterThan(targetTime)) {
                return i;
            }
        }
        return 0;
    }

    public void setUpperSubtitleMode(String upperSubtitleMode) {
        this.upperSubtitleMode = upperSubtitleMode;
    }

    public void setLowerSubtitleMode(String lowerSubtitleMode) {
        this.lowerSubtitleMode = lowerSubtitleMode;
    }

    public String getUpperSubtitle(MediaPlayer.Status currentStatus) {
        if (currentSubtitle == null) {
            return "";
        }

        return switch (upperSubtitleMode) {
            case ORIGINAL_ALWAYS -> currentSubtitle.getContent();
            case ORIGINAL_ON_PAUSE_ONLY -> currentStatus.equals(MediaPlayer.Status.PAUSED) ? currentSubtitle.getContent() : "";
            case TRANSLATED_ALWAYS -> currentSubtitle.getContentTranslation();
            case TRANSLATED_ON_PAUSE_ONLY -> currentStatus.equals(MediaPlayer.Status.PAUSED) ? currentSubtitle.getContentTranslation() : "";
            default -> "";
        };
    }

    public String getLowerSubtitle(MediaPlayer.Status currentStatus) {
        if (currentSubtitle == null) {
            return "";
        }

        return switch (lowerSubtitleMode) {
            case ORIGINAL_ALWAYS -> currentSubtitle.getContent();
            case ORIGINAL_ON_PAUSE_ONLY -> currentStatus.equals(MediaPlayer.Status.PAUSED) ? currentSubtitle.getContent() : "";
            case TRANSLATED_ALWAYS -> currentSubtitle.getContentTranslation();
            case TRANSLATED_ON_PAUSE_ONLY -> currentStatus.equals(MediaPlayer.Status.PAUSED) ? currentSubtitle.getContentTranslation() : "";
            default -> "";
        };
    }

    public static class Mode {

        public static final String ORIGINAL_ALWAYS = "Original - always";
        public static final String ORIGINAL_ON_PAUSE_ONLY = "Original - only on pause";
        public static final String TRANSLATED_ALWAYS = "Translated - always";
        public static final String TRANSLATED_ON_PAUSE_ONLY = "Translated - only on pause";
        public static final String NONE = "None";

        public static final String[] MODE_LIST = {ORIGINAL_ALWAYS, ORIGINAL_ON_PAUSE_ONLY, TRANSLATED_ALWAYS, TRANSLATED_ON_PAUSE_ONLY, NONE};

    }

}
