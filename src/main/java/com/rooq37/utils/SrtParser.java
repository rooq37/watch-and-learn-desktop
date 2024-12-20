package com.rooq37.utils;


import com.rooq37.model.SubtitleEntity;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SrtParser {

    private final String path;
    private String pathTranslated;
    private List<SubtitleEntity> subtitleEntityList;

    public SrtParser(String path) {
        this.path = path;
    }

    public SrtParser(String path, String pathTranslated) {
        this.path = path;
        this.pathTranslated = pathTranslated;
    }

    public void read() throws IOException {
        this.subtitleEntityList = this.read(path);
        if (pathTranslated != null) {
            List<SubtitleEntity> translatedEntities = this.read(pathTranslated);
            if (subtitleEntityList.size() != translatedEntities.size()) throw new RuntimeException("Subtitles don't match");
            for (int i = 0; i < subtitleEntityList.size(); i++) {
                SubtitleEntity original = subtitleEntityList.get(i);
                SubtitleEntity translated = translatedEntities.get(i);
                if (original.getStart().equals(translated.getStart()) && original.getStop().equals(translated.getStop())) {
                    subtitleEntityList.get(i).setContentTranslation(translatedEntities.get(i).getContent());
                } else {
                    throw new RuntimeException("Subtitles don't match");
                }
            }
        }
    }

    private List<SubtitleEntity> read(String path) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            List<String> entries = List.of(br.lines().collect(Collectors.joining("\n")).split("\n\n"));
            return entries.stream().map(this::mapToSubtitleEntity).collect(Collectors.toList());
        }
    }

    public List<SubtitleEntity> getSubtitleEntityList() {
        return subtitleEntityList;
    }

    private SubtitleEntity mapToSubtitleEntity(String entry) {
        String searchString = "([0-9]*)\\n(\\d{2}:\\d{2}:\\d{2},\\d{3}) --> (\\d{2}:\\d{2}:\\d{2},\\d{3})\\n([\\S\\s]*)";
        Pattern pattern = Pattern.compile(searchString);
        Matcher matcher = pattern.matcher(entry);
        matcher.find();

        SubtitleEntity subtitleEntity = new SubtitleEntity(parseSrtDuration(matcher.group(2)), parseSrtDuration(matcher.group(3)));
        subtitleEntity.setContent(matcher.group(4).replaceAll("</font>", "").replaceAll("<font[^>]*>", ""));
        return subtitleEntity;
    }

    private Duration parseSrtDuration(String durationString) {
        String searchString = "(\\d{2}):(\\d{2}):(\\d{2}),(\\d{3})";
        Pattern pattern = Pattern.compile(searchString, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(durationString);
        matcher.find();

        long millis = Long.parseLong(matcher.group(4));
        millis += Long.parseLong(matcher.group(3)) * 1000;
        millis += Long.parseLong(matcher.group(2)) * 60 * 1000;
        millis += Long.parseLong(matcher.group(1)) * 60 * 60 * 1000;

        return new Duration(millis);
    }

}
