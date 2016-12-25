package me.matthewmcmillan.wcaanalyzer;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ErrorMessageController {
    private Stage thisStage;
    @FXML
    Button okButton;

    @FXML
    Label errorMessage;

    public void closePopUpAndReset() {
        thisStage.close();
        Main.mainStage.centerOnScreen();
        Main.mainController.reset();
    }

    public void setErrorMessage(String message) {
        errorMessage.setText(message);
    }

    public void setStage(Stage stage) {
        thisStage = stage;
    }
}
