package com.rooq37.model;

import javafx.util.Duration;

public class SubtitleEntity {

    private final Duration start;
    private final Duration stop;
    private String content;
    private String contentTranslation;

    public SubtitleEntity(Duration start, Duration stop) {
        this.start = start;
        this.stop = stop;
    }

    public Duration getStart() {
        return start;
    }

    public Duration getStop() {
        return stop;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getContentTranslation() {
        return contentTranslation;
    }

    public void setContentTranslation(String contentTranslation) {
        this.contentTranslation = contentTranslation;
    }

    @Override
    public String toString() {
        return "SubtitleEntity{" +
                "start=" + start +
                ", stop=" + stop +
                ", content='" + content + '\'' +
                '}';
    }
}
