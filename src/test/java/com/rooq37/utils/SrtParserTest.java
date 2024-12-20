package com.rooq37.utils;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

class SrtParserTest {

    private static final String SUBTITLES_EN_PATH = new File("src/test/resources/test-subtitles-en.srt").getAbsolutePath();
    private static final String SUBTITLES_PL_PATH = new File("src/test/resources/test-subtitles-pl.srt").getAbsolutePath();
    private static final String SUBTITLES_PL_NOT_MATCHING_SIZE_PATH = new File("src/test/resources/not-matching-size-test-subtitles-pl.srt").getAbsolutePath();
    private static final String SUBTITLES_PL_NOT_MATCHING_TIMESTAMP_PATH = new File("src/test/resources/not-matching-timestamp-test-subtitles-pl.srt").getAbsolutePath();

    @Test
    public void subtitlesWithoutTranslationsTest() throws IOException {
        SrtParser srtParser = new SrtParser(SUBTITLES_EN_PATH);
        srtParser.read();

        assertThat(srtParser.getSubtitleEntityList().size()).isEqualTo(10);
        assertThat(srtParser.getSubtitleEntityList().get(0).getStart().toString()).isEqualTo("4560.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(0).getStop().toString()).isEqualTo("6720.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(0).getContent()).isEqualTo("Subs one.");
        assertThat(srtParser.getSubtitleEntityList().get(0).getContentTranslation()).isNull();
        assertThat(srtParser.getSubtitleEntityList().get(1).getStart().toString()).isEqualTo("6720.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(1).getStop().toString()).isEqualTo("9320.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(1).getContent()).isEqualTo("Subs two.");
        assertThat(srtParser.getSubtitleEntityList().get(1).getContentTranslation()).isNull();
        assertThat(srtParser.getSubtitleEntityList().get(2).getStart().toString()).isEqualTo("9320.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(2).getStop().toString()).isEqualTo("12040.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(2).getContent()).isEqualTo("Subs three.");
        assertThat(srtParser.getSubtitleEntityList().get(2).getContentTranslation()).isNull();
        assertThat(srtParser.getSubtitleEntityList().get(3).getStart().toString()).isEqualTo("12040.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(3).getStop().toString()).isEqualTo("13680.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(3).getContent()).isEqualTo("Subs four.");
        assertThat(srtParser.getSubtitleEntityList().get(3).getContentTranslation()).isNull();
        assertThat(srtParser.getSubtitleEntityList().get(4).getStart().toString()).isEqualTo("13680.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(4).getStop().toString()).isEqualTo("18320.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(4).getContent()).isEqualTo("Subs five part one\nsubs five part two.");
        assertThat(srtParser.getSubtitleEntityList().get(4).getContentTranslation()).isNull();
        assertThat(srtParser.getSubtitleEntityList().get(5).getStart().toString()).isEqualTo("18320.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(5).getStop().toString()).isEqualTo("21600.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(5).getContent()).isEqualTo("Subs six.");
        assertThat(srtParser.getSubtitleEntityList().get(5).getContentTranslation()).isNull();
        assertThat(srtParser.getSubtitleEntityList().get(6).getStart().toString()).isEqualTo("21600.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(6).getStop().toString()).isEqualTo("24240.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(6).getContent()).isEqualTo("Subs seven part one\nsubs seven part two.");
        assertThat(srtParser.getSubtitleEntityList().get(6).getContentTranslation()).isNull();
        assertThat(srtParser.getSubtitleEntityList().get(7).getStart().toString()).isEqualTo("24240.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(7).getStop().toString()).isEqualTo("26000.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(7).getContent()).isEqualTo("Subs eight.");
        assertThat(srtParser.getSubtitleEntityList().get(7).getContentTranslation()).isNull();
        assertThat(srtParser.getSubtitleEntityList().get(8).getStart().toString()).isEqualTo("26000.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(8).getStop().toString()).isEqualTo("27600.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(8).getContent()).isEqualTo("Subs nine.");
        assertThat(srtParser.getSubtitleEntityList().get(8).getContentTranslation()).isNull();
        assertThat(srtParser.getSubtitleEntityList().get(9).getStart().toString()).isEqualTo("27600.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(9).getStop().toString()).isEqualTo("29320.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(9).getContent()).isEqualTo("Subs ten.");
        assertThat(srtParser.getSubtitleEntityList().get(9).getContentTranslation()).isNull();
    }

    @Test
    public void subtitlesWithTranslationsTest() throws IOException {
        SrtParser srtParser = new SrtParser(SUBTITLES_EN_PATH, SUBTITLES_PL_PATH);
        srtParser.read();

        assertThat(srtParser.getSubtitleEntityList().size()).isEqualTo(10);
        assertThat(srtParser.getSubtitleEntityList().get(0).getStart().toString()).isEqualTo("4560.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(0).getStop().toString()).isEqualTo("6720.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(0).getContent()).isEqualTo("Subs one.");
        assertThat(srtParser.getSubtitleEntityList().get(0).getContentTranslation()).isEqualTo("Napisy jeden.");
        assertThat(srtParser.getSubtitleEntityList().get(1).getStart().toString()).isEqualTo("6720.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(1).getStop().toString()).isEqualTo("9320.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(1).getContent()).isEqualTo("Subs two.");
        assertThat(srtParser.getSubtitleEntityList().get(1).getContentTranslation()).isEqualTo("Napisy dwa.");
        assertThat(srtParser.getSubtitleEntityList().get(2).getStart().toString()).isEqualTo("9320.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(2).getStop().toString()).isEqualTo("12040.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(2).getContent()).isEqualTo("Subs three.");
        assertThat(srtParser.getSubtitleEntityList().get(2).getContentTranslation()).isEqualTo("Napisy trzy.");
        assertThat(srtParser.getSubtitleEntityList().get(3).getStart().toString()).isEqualTo("12040.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(3).getStop().toString()).isEqualTo("13680.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(3).getContent()).isEqualTo("Subs four.");
        assertThat(srtParser.getSubtitleEntityList().get(3).getContentTranslation()).isEqualTo("Napisy cztery.");
        assertThat(srtParser.getSubtitleEntityList().get(4).getStart().toString()).isEqualTo("13680.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(4).getStop().toString()).isEqualTo("18320.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(4).getContent()).isEqualTo("Subs five part one\nsubs five part two.");
        assertThat(srtParser.getSubtitleEntityList().get(4).getContentTranslation()).isEqualTo("Napisy pięć.");
        assertThat(srtParser.getSubtitleEntityList().get(5).getStart().toString()).isEqualTo("18320.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(5).getStop().toString()).isEqualTo("21600.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(5).getContent()).isEqualTo("Subs six.");
        assertThat(srtParser.getSubtitleEntityList().get(5).getContentTranslation()).isEqualTo("Napisy sześć.");
        assertThat(srtParser.getSubtitleEntityList().get(6).getStart().toString()).isEqualTo("21600.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(6).getStop().toString()).isEqualTo("24240.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(6).getContent()).isEqualTo("Subs seven part one\nsubs seven part two.");
        assertThat(srtParser.getSubtitleEntityList().get(6).getContentTranslation()).isEqualTo("Napisy siedem.");
        assertThat(srtParser.getSubtitleEntityList().get(7).getStart().toString()).isEqualTo("24240.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(7).getStop().toString()).isEqualTo("26000.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(7).getContent()).isEqualTo("Subs eight.");
        assertThat(srtParser.getSubtitleEntityList().get(7).getContentTranslation()).isEqualTo("Napisy osiem.");
        assertThat(srtParser.getSubtitleEntityList().get(8).getStart().toString()).isEqualTo("26000.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(8).getStop().toString()).isEqualTo("27600.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(8).getContent()).isEqualTo("Subs nine.");
        assertThat(srtParser.getSubtitleEntityList().get(8).getContentTranslation()).isEqualTo("Napisy dziewięć.");
        assertThat(srtParser.getSubtitleEntityList().get(9).getStart().toString()).isEqualTo("27600.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(9).getStop().toString()).isEqualTo("29320.0 ms");
        assertThat(srtParser.getSubtitleEntityList().get(9).getContent()).isEqualTo("Subs ten.");
        assertThat(srtParser.getSubtitleEntityList().get(9).getContentTranslation()).isEqualTo("Napisy dziesięć.");
    }

    @Test
    public void subtitlesNotMatchingSizeTest() {
        SrtParser srtParser = new SrtParser(SUBTITLES_EN_PATH, SUBTITLES_PL_NOT_MATCHING_SIZE_PATH);

        assertThatThrownBy(srtParser::read)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Subtitles don't match");
    }

    @Test
    public void subtitlesNotMatchingTimestampTest() {
        SrtParser srtParser = new SrtParser(SUBTITLES_EN_PATH, SUBTITLES_PL_NOT_MATCHING_TIMESTAMP_PATH);

        assertThatThrownBy(srtParser::read)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Subtitles don't match");
    }

}