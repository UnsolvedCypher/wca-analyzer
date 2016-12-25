package me.matthewmcmillan.wcaanalyzer;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class ErrorMessagePopup {
    public ErrorMessagePopup (String message) {
        try {
            URL location = getClass().getResource("ErrorMessage.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent root = (Parent) fxmlLoader.load(location.openStream());
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            ErrorMessageController controller = fxmlLoader.getController();
            controller.setErrorMessage(message);
            controller.setStage(stage);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error error lol");
        }
    }
}
