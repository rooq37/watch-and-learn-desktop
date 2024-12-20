package com.rooq37.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    private Stage primaryStage;

    @FXML
    private TextField videoPathInput, originalSubtitlesPathInput, translatedSubtitlesPathInput;
    @FXML
    private ChoiceBox<String> upperSubtitlesModeChoiceBox, lowerSubtitlesModeChoiceBox;
    @FXML
    private Button continueButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        continueButton.setDisable(true);

        upperSubtitlesModeChoiceBox.setItems(FXCollections.observableArrayList(SubtitlesManager.Mode.MODE_LIST));
        upperSubtitlesModeChoiceBox.getSelectionModel().select(SubtitlesManager.Mode.TRANSLATED_ON_PAUSE_ONLY);

        lowerSubtitlesModeChoiceBox.setItems(FXCollections.observableArrayList(SubtitlesManager.Mode.MODE_LIST));
        lowerSubtitlesModeChoiceBox.getSelectionModel().select(SubtitlesManager.Mode.ORIGINAL_ALWAYS);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void videoPathButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP4 file", "*.mp4"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            videoPathInput.setText(selectedFile.getAbsolutePath());
            continueButton.setDisable(!checkIfCanContinue());
        }
    }

    public void originalSubtitlesPathButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Original subtitles file", "*.srt"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            originalSubtitlesPathInput.setText(selectedFile.getAbsolutePath());
            continueButton.setDisable(!checkIfCanContinue());
        }
    }

    public void translatedSubtitlesPathButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Translated subtitles file", "*.srt"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            translatedSubtitlesPathInput.setText(selectedFile.getAbsolutePath());
            continueButton.setDisable(!checkIfCanContinue());
        }
    }

    private boolean checkIfCanContinue() {
        return !videoPathInput.getText().isEmpty() && !originalSubtitlesPathInput.getText().isEmpty() && !translatedSubtitlesPathInput.getText().isEmpty();
    }

    public void onContinueButtonAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/player.fxml"));
        Parent root = loader.load(); // Note the loader must be loaded before you can access the controller.
        PlayerController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);
        controller.postInitialize(videoPathInput.getText(), originalSubtitlesPathInput.getText(), translatedSubtitlesPathInput.getText(),
                upperSubtitlesModeChoiceBox.getValue(), lowerSubtitlesModeChoiceBox.getValue());
        primaryStage.setScene(new Scene(root));
    }
}
