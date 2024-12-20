package com.rooq37.controllers;

import com.rooq37.utils.SrtParser;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class SubtitlesManagerTest {

    private static final String SUBTITLES_EN_PATH = new File("src/test/resources/test-subtitles-en.srt").getAbsolutePath();
    private static final String SUBTITLES_PL_PATH = new File("src/test/resources/test-subtitles-pl.srt").getAbsolutePath();

    @Test
    void proceedAlways() throws IOException {
        SrtParser srtParser = new SrtParser(SUBTITLES_EN_PATH, SUBTITLES_PL_PATH);
        srtParser.read();

        SubtitlesManager subtitlesManager = new SubtitlesManager(srtParser.getSubtitleEntityList());
        subtitlesManager.setUpperSubtitleMode(SubtitlesManager.Mode.TRANSLATED_ALWAYS);
        subtitlesManager.setLowerSubtitleMode(SubtitlesManager.Mode.ORIGINAL_ALWAYS);

        for (double seconds = 0; seconds <= 4.000; seconds += 0.01) subtitlesManager.proceed(Duration.seconds(seconds));
        subtitlesManager.proceed(Duration.seconds(4.010));
        assertThat(subtitlesManager.getUpperSubtitle(MediaPlayer.Status.PLAYING)).isEmpty();
        assertThat(subtitlesManager.getLowerSubtitle(MediaPlayer.Status.PLAYING)).isEmpty();

        for (double seconds = 4.020; seconds <= 4.600; seconds += 0.01) subtitlesManager.proceed(Duration.seconds(seconds));
        subtitlesManager.proceed(Duration.seconds(4.610));
        assertThat(subtitlesManager.getUpperSubtitle(MediaPlayer.Status.PLAYING)).isEqualTo("Napisy jeden.");
        assertThat(subtitlesManager.getLowerSubtitle(MediaPlayer.Status.PLAYING)).isEqualTo("Subs one.");

        for (double seconds = 4.620; seconds <= 6.720; seconds += 0.01) subtitlesManager.proceed(Duration.seconds(seconds));
        subtitlesManager.proceed(Duration.seconds(6.730));
        assertThat(subtitlesManager.getUpperSubtitle(MediaPlayer.Status.PLAYING)).isEqualTo("Napisy dwa.");
        assertThat(subtitlesManager.getLowerSubtitle(MediaPlayer.Status.PLAYING)).isEqualTo("Subs two.");

        for (double seconds = 6.740; seconds <= 27.600; seconds += 0.01) subtitlesManager.proceed(Duration.seconds(seconds));
        subtitlesManager.proceed(Duration.seconds(27.610));
        assertThat(subtitlesManager.getUpperSubtitle(MediaPlayer.Status.PLAYING)).isEqualTo("Napisy dziesięć.");
        assertThat(subtitlesManager.getLowerSubtitle(MediaPlayer.Status.PLAYING)).isEqualTo("Subs ten.");
    }

    @Test
    void proceedOnPauseOnly() throws IOException {
        SrtParser srtParser = new SrtParser(SUBTITLES_EN_PATH, SUBTITLES_PL_PATH);
        srtParser.read();

        SubtitlesManager subtitlesManager = new SubtitlesManager(srtParser.getSubtitleEntityList());
        subtitlesManager.setUpperSubtitleMode(SubtitlesManager.Mode.TRANSLATED_ON_PAUSE_ONLY);
        subtitlesManager.setLowerSubtitleMode(SubtitlesManager.Mode.ORIGINAL_ON_PAUSE_ONLY);

        for (double seconds = 0; seconds <= 4.000; seconds += 0.01) subtitlesManager.proceed(Duration.seconds(seconds));
        subtitlesManager.proceed(Duration.seconds(4.010));
        assertThat(subtitlesManager.getUpperSubtitle(MediaPlayer.Status.PLAYING)).isEmpty();
        assertThat(subtitlesManager.getLowerSubtitle(MediaPlayer.Status.PLAYING)).isEmpty();

        for (double seconds = 4.020; seconds <= 4.600; seconds += 0.01) subtitlesManager.proceed(Duration.seconds(seconds));
        subtitlesManager.proceed(Duration.seconds(4.610));
        assertThat(subtitlesManager.getUpperSubtitle(MediaPlayer.Status.PLAYING)).isEmpty();
        assertThat(subtitlesManager.getLowerSubtitle(MediaPlayer.Status.PLAYING)).isEmpty();

        for (double seconds = 4.620; seconds <= 6.720; seconds += 0.01) subtitlesManager.proceed(Duration.seconds(seconds));
        subtitlesManager.proceed(Duration.seconds(6.730));
        assertThat(subtitlesManager.getUpperSubtitle(MediaPlayer.Status.PAUSED)).isEqualTo("Napisy dwa.");
        assertThat(subtitlesManager.getLowerSubtitle(MediaPlayer.Status.PAUSED)).isEqualTo("Subs two.");

        for (double seconds = 6.740; seconds <= 27.600; seconds += 0.01) subtitlesManager.proceed(Duration.seconds(seconds));
        subtitlesManager.proceed(Duration.seconds(27.610));
        assertThat(subtitlesManager.getUpperSubtitle(MediaPlayer.Status.PAUSED)).isEqualTo("Napisy dziesięć.");
        assertThat(subtitlesManager.getLowerSubtitle(MediaPlayer.Status.PAUSED)).isEqualTo("Subs ten.");
    }

    @Test
    void proceedMixed() throws IOException {
        SrtParser srtParser = new SrtParser(SUBTITLES_EN_PATH, SUBTITLES_PL_PATH);
        srtParser.read();

        SubtitlesManager subtitlesManager = new SubtitlesManager(srtParser.getSubtitleEntityList());
        subtitlesManager.setUpperSubtitleMode(SubtitlesManager.Mode.TRANSLATED_ON_PAUSE_ONLY);
        subtitlesManager.setLowerSubtitleMode(SubtitlesManager.Mode.ORIGINAL_ALWAYS);

        for (double seconds = 0; seconds <= 4.000; seconds += 0.01) subtitlesManager.proceed(Duration.seconds(seconds));
        subtitlesManager.proceed(Duration.seconds(4.010));
        assertThat(subtitlesManager.getUpperSubtitle(MediaPlayer.Status.PLAYING)).isEmpty();
        assertThat(subtitlesManager.getLowerSubtitle(MediaPlayer.Status.PLAYING)).isEmpty();

        for (double seconds = 4.020; seconds <= 4.600; seconds += 0.01) subtitlesManager.proceed(Duration.seconds(seconds));
        subtitlesManager.proceed(Duration.seconds(4.610));
        assertThat(subtitlesManager.getUpperSubtitle(MediaPlayer.Status.PLAYING)).isEmpty();
        assertThat(subtitlesManager.getLowerSubtitle(MediaPlayer.Status.PLAYING)).isEqualTo("Subs one.");

        for (double seconds = 4.620; seconds <= 6.720; seconds += 0.01) subtitlesManager.proceed(Duration.seconds(seconds));
        subtitlesManager.proceed(Duration.seconds(6.730));
        assertThat(subtitlesManager.getUpperSubtitle(MediaPlayer.Status.PAUSED)).isEqualTo("Napisy dwa.");
        assertThat(subtitlesManager.getLowerSubtitle(MediaPlayer.Status.PAUSED)).isEqualTo("Subs two.");

        for (double seconds = 6.740; seconds <= 27.600; seconds += 0.01) subtitlesManager.proceed(Duration.seconds(seconds));
        subtitlesManager.proceed(Duration.seconds(27.610));
        assertThat(subtitlesManager.getUpperSubtitle(MediaPlayer.Status.PLAYING)).isEmpty();
        assertThat(subtitlesManager.getLowerSubtitle(MediaPlayer.Status.PLAYING)).isEqualTo("Subs ten.");
    }


    @Test
    void seek() throws IOException {
        SrtParser srtParser = new SrtParser(SUBTITLES_EN_PATH, SUBTITLES_PL_PATH);
        srtParser.read();

        SubtitlesManager subtitlesManager = new SubtitlesManager(srtParser.getSubtitleEntityList());
        subtitlesManager.setUpperSubtitleMode(SubtitlesManager.Mode.TRANSLATED_ALWAYS);
        subtitlesManager.setLowerSubtitleMode(SubtitlesManager.Mode.ORIGINAL_ALWAYS);

        for (double seconds = 0; seconds <= 4.000; seconds += 0.01) subtitlesManager.proceed(Duration.seconds(seconds));
        subtitlesManager.proceed(Duration.seconds(4.010));
        assertThat(subtitlesManager.getUpperSubtitle(MediaPlayer.Status.PLAYING)).isEmpty();
        assertThat(subtitlesManager.getLowerSubtitle(MediaPlayer.Status.PLAYING)).isEmpty();

        for (double seconds = 4.020; seconds <= 4.600; seconds += 0.01) subtitlesManager.proceed(Duration.seconds(seconds));
        subtitlesManager.proceed(Duration.seconds(4.610));
        assertThat(subtitlesManager.getUpperSubtitle(MediaPlayer.Status.PLAYING)).isEqualTo("Napisy jeden.");
        assertThat(subtitlesManager.getLowerSubtitle(MediaPlayer.Status.PLAYING)).isEqualTo("Subs one.");

        subtitlesManager.seek(Duration.seconds(27.610));
        assertThat(subtitlesManager.getUpperSubtitle(MediaPlayer.Status.PLAYING)).isEqualTo("Napisy dziesięć.");
        assertThat(subtitlesManager.getLowerSubtitle(MediaPlayer.Status.PLAYING)).isEqualTo("Subs ten.");

        subtitlesManager.seek(Duration.seconds(6.730));
        assertThat(subtitlesManager.getUpperSubtitle(MediaPlayer.Status.PLAYING)).isEqualTo("Napisy dwa.");
        assertThat(subtitlesManager.getLowerSubtitle(MediaPlayer.Status.PLAYING)).isEqualTo("Subs two.");
    }
}