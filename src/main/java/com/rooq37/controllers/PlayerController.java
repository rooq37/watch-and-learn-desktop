package com.rooq37.controllers;

import com.rooq37.utils.DurationFormatter;
import com.rooq37.utils.SrtParser;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PlayerController implements Initializable {

    private static final String[] selectableRates = {"0.8x", "0.9x", "1.0x", "1.1x", "1.2x"};

    private Stage primaryStage;

    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private VBox toolsVBox;
    @FXML
    private HBox buttonsHBox;
    @FXML
    private MediaView mediaView;
    @FXML
    private Label currentTimeLabel, durationLabel;
    @FXML
    private Slider progressSlider;
    @FXML
    private ChoiceBox<String> rateChoiceBox;
    @FXML
    private TextArea topSubtitlesTextArea, bottomSubtitlesTextArea;
    @FXML
    private Button playButton, pauseButton;

    private MediaPlayer mediaPlayer;
    private SubtitlesManager subtitlesManager;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainBorderPane.widthProperty().addListener((observableValue, oldValue, newValue) ->
                mediaView.setFitWidth(newValue.doubleValue()));

        mainBorderPane.heightProperty().addListener((observableValue, oldValue, newValue) -> {
            mediaView.setFitHeight(newValue.doubleValue() - toolsVBox.getHeight());
            if (newValue.intValue() > 800) {
                topSubtitlesTextArea.setFont(Font.font(36));
                bottomSubtitlesTextArea.setFont(Font.font(36));
            } else {
                topSubtitlesTextArea.setFont(Font.font(24));
                bottomSubtitlesTextArea.setFont(Font.font(24));
            }
        });

        rateChoiceBox.setItems(FXCollections.observableArrayList(selectableRates));
        rateChoiceBox.getSelectionModel().select(selectableRates[2]);
        rateChoiceBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            double selectedValue = Double.parseDouble(newValue.replace("x", ""));
            mediaPlayer.setRate(selectedValue);
        });
    }

    public void postInitialize(String videoPath, String originalSubtitlesPath, String translatedSubtitlesPath, String upperSubtitlesMode, String lowerSubtitlesMode) {
        mediaPlayer = new MediaPlayer(new Media(new File(videoPath).toURI().toASCIIString()));
        mediaView.setMediaPlayer(mediaPlayer);

        initializePlayer();

        try {
            SrtParser srtParser = translatedSubtitlesPath.isEmpty() ? new SrtParser(originalSubtitlesPath) : new SrtParser(originalSubtitlesPath, translatedSubtitlesPath);
            srtParser.read();
            subtitlesManager = new SubtitlesManager(srtParser.getSubtitleEntityList());
            subtitlesManager.setUpperSubtitleMode(upperSubtitlesMode);
            subtitlesManager.setLowerSubtitleMode(lowerSubtitlesMode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        primaryStage.getScene().addEventFilter(KeyEvent.KEY_RELEASED, keyEvent -> {
            switch (keyEvent.getCode()) {
                case RIGHT -> nextSegment();
                case LEFT -> previousSegment();
                case UP -> nextSubtitle();
                case DOWN -> previousSubtitle();
                case SPACE -> playOrPause();
            }
        });
    }

    private void initializePlayer() {
        mediaPlayer.setOnReady(() -> {

            durationLabel.setText(DurationFormatter.format(mediaPlayer.getTotalDuration()));
            progressSlider.setMax(mediaPlayer.getTotalDuration().toSeconds());
            progressSlider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
                if (Math.abs(newValue.doubleValue() - oldValue.doubleValue()) > 1) {
                    seek(newValue.doubleValue());
                }
            });
            mediaPlayer.play();
        });

        mediaPlayer.currentTimeProperty().addListener((observableValue, oldValue, newValue) -> {
            currentTimeLabel.setText(DurationFormatter.format(newValue));
            progressSlider.setValue(newValue.toSeconds());
            updateSubtitles(newValue);
        });

        mediaPlayer.statusProperty().addListener((observableValue, oldStatus, newStatus) -> {
            switch (newStatus) {
                case PLAYING -> {
                    buttonsHBox.getChildren().remove(playButton);
                    if (!buttonsHBox.getChildren().contains(pauseButton)) {
                        buttonsHBox.getChildren().add(2, pauseButton);
                    }
                }
                case PAUSED -> {
                    buttonsHBox.getChildren().remove(pauseButton);
                    buttonsHBox.getChildren().add(2, playButton);
                }
            }
            updateSubtitles(mediaPlayer.getCurrentTime());
        });
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void onActionPlayPauseButton(ActionEvent event) {
        playOrPause();
    }

    public void onActionPreviousSegmentButton(ActionEvent event) {
        previousSegment();
    }

    public void onActionNextSegmentButton(ActionEvent event) {
        nextSegment();
    }

    public void onActionPreviousSubtitleButton(ActionEvent event) {
        previousSubtitle();
    }

    public void onActionNextSubtitleButton(ActionEvent event) {
        nextSubtitle();
    }

    public void onActionFullscreenButton(ActionEvent event) {
        this.primaryStage.setFullScreen(!this.primaryStage.isFullScreen());
    }

    private void playOrPause() {
        switch (mediaPlayer.getStatus()) {
            case PLAYING -> mediaPlayer.pause();
            case PAUSED -> mediaPlayer.play();
        }
    }

    private void previousSegment() {
        double currentTime = mediaPlayer.getCurrentTime().toSeconds();
        this.seek(currentTime - 10);
    }

    private void nextSegment() {
        double currentTime = mediaPlayer.getCurrentTime().toSeconds();
        this.seek(currentTime + 10);
    }

    private void previousSubtitle() {
        this.seek(subtitlesManager.jumpToPreviousSubtitle().toSeconds());
    }

    private void nextSubtitle() {
        this.seek(subtitlesManager.jumpToNextSubtitle().toSeconds());
    }

    private void seek(double seconds) {
        subtitlesManager.seek(Duration.seconds(seconds));
        mediaPlayer.seek(Duration.seconds(seconds));
    }

    private void updateSubtitles(Duration currentTime) {
        subtitlesManager.proceed(currentTime);
        String upperSubtitle = subtitlesManager.getUpperSubtitle(mediaPlayer.getStatus());
        topSubtitlesTextArea.setPrefRowCount(upperSubtitle.split("\n").length);
        topSubtitlesTextArea.setText(upperSubtitle);
        String lowerSubtitle = subtitlesManager.getLowerSubtitle(mediaPlayer.getStatus());
        bottomSubtitlesTextArea.setPrefRowCount(lowerSubtitle.split("\n").length);
        bottomSubtitlesTextArea.setText(lowerSubtitle);
    }

}
