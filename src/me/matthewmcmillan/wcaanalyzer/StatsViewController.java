package me.matthewmcmillan.wcaanalyzer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class StatsViewController {
    private Stage thisStage;

    @FXML
    TabPane tabPane;

    @FXML
    Label competitorName;

    @FXML
    public void initialize() {
        try {
            competitorName.setText("Stats for " + Main.competitorName + " (" + Main.WCAID + ")");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("General.fxml"));
            loader.load();
            GeneralController controller = loader.getController();
            tabPane.getTabs().add(controller.getTab());

            for (Event event : Main.events) {
                loader = new FXMLLoader(getClass().getResource("EventTab.fxml"));
                loader.load();
                EventTabController eventTabController = loader.getController();
                tabPane.getTabs().add(eventTabController.getTab(event));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setStage(Stage stage) {
        thisStage = stage;
    }

    @FXML
    public void returnToStart() {
        thisStage.setScene(Main.mainScene);
        thisStage.centerOnScreen();
    }
}
