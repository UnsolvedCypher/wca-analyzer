package me.matthewmcmillan.wcaanalyzer;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class StartPageController {

    Stage thisStage;

    public void setStage (Stage stage){
        thisStage = stage;
    }

    public void showStage(){
        thisStage.setTitle("WCA Analyzer");
        thisStage.show();
    }

    @FXML
    private TextField idField;

    @FXML
    private ProgressIndicator progress;

    @FXML
    private Label loadingMsg;

    private void switchToStats() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StatsView.fxml"));
            Parent stats = loader.load();
            StatsViewController controller = loader.getController();
            thisStage.setScene(new Scene(stats, 800, 600));
            thisStage.setMaximized(true);
            controller.setStage(thisStage);
            reset();
        } catch (Exception e) {
            System.out.print("Error switching to stats view");
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public void getStats() {
        progress.setVisible(true);
        loadingMsg.setVisible(true);
        Task<Void> readData = new Task<Void>() {
            @Override
            protected Void call()  throws Exception{
                WCAReader reader = new WCAReader(idField.getText());
                Main.WCAID = idField.getText().toUpperCase();
                Main.competitorName = reader.getName();
                reader.readComps();
                reader.readEvents(Main.comps);
                reader.getYearTreeMap(Main.comps);
                return null;
            }
        };
        readData.setOnSucceeded(e -> switchToStats());
        readData.setOnFailed(e -> {
            readData.getException().printStackTrace();
            new ErrorMessagePopup(readData.getException().getMessage());});
        new Thread(readData).start();
    }
    public void reset() {
        progress.setVisible(false);
        loadingMsg.setVisible(false);
        idField.setText("");
    }
}
