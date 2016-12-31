package me.matthewmcmillan.wcaanalyzer;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.util.function.BiFunction;


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
    private ProgressIndicator progressIndicator;

    @FXML
    private Label loadingMsg, version;

    @FXML
    private ProgressBar progressBar;

    public void initialize() {
        version.setText("Version " + Main.VERSION);
    }

    private void switchToStats() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StatsView.fxml"));
            Parent stats = loader.load();
            StatsViewController controller = loader.getController();
            Stage newStage = new Stage();
            controller.setStage(newStage);
            newStage.setScene(new Scene(stats, 800, 600));
            newStage.setMaximized(true);
            newStage.setTitle("WCA Analyzer");
            newStage.show();
            thisStage.hide();
            reset();
        } catch (Exception e) {
            System.out.print("Error switching to stats view");
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public void getStats() {
        progressIndicator.setVisible(true);
        loadingMsg.setVisible(true);
        Task<Void> readData = new Task<Void>() {
            @Override
            protected Void call()  throws Exception{
                updateProgress(0, 1);
                WCAReader reader = new WCAReader(idField.getText());
                Main.WCAID = idField.getText().toUpperCase();
                Main.competitorName = reader.getName();
                BiFunction<Double, Double, Void> callableUpdate = (p, v) -> {
                    updateProgress(p, v);
                    return null;
                };
                Task<Void> readCompsTask = reader.readComps(callableUpdate);
                readCompsTask.setOnFailed(e -> {
                    readCompsTask.getException().printStackTrace();
                    new ErrorMessagePopup(readCompsTask.getException().getMessage());});
                readCompsTask.run();

                reader.readEvents(Main.comps);
                reader.getYearTreeMap(Main.comps);
                return null;
            }
        };
        readData.setOnSucceeded(e -> switchToStats());
        readData.setOnFailed(e -> {
            readData.getException().printStackTrace();
            new ErrorMessagePopup(readData.getException().getMessage());});

        progressBar.progressProperty().bind(readData.progressProperty());
        new Thread(readData).start();
    }
    public void reset() {
        progressIndicator.setVisible(false);
        progressBar.progressProperty().unbind();
        progressBar.setProgress(0);
        loadingMsg.setVisible(false);
        idField.setText("");
    }
}
