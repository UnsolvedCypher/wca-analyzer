package me.matthewmcmillan.wcaanalyzer;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.TreeMap;

public class StatsViewController {
    private Stage thisStage;

    @FXML
    TabPane tabPane;

    @FXML
    Label competitorName, version;

    @FXML
    Tab generalTab;

    @FXML
    TableView<Integer> compYearTable;

    @FXML
    TableView<PBStreak> pbStreakTable;

    @FXML
    TableColumn<Integer, String> compCol, yearCol, daysBetweenCol;

    @FXML
    TableColumn<PBStreak, String> lengthCol, startCol, endCol;

    @FXML
    CheckBox excludeFMCCheck;

    @FXML
    public void initialize() {
        try {
            version.setText("Version " + Main.VERSION);
            competitorName.setText("Stats for " + Main.competitorName + " (" + Main.WCAID + ")");
            setTables();
            for (Event event : Main.events) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("EventsTabWithGraphs.fxml"));
                loader.load();
                EventTabController eventTabController = loader.getController();
                tabPane.getTabs().add(eventTabController.getTab(event));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ObservableList<PBStreak> streaks = FXCollections.observableArrayList();
    ObservableList<PBStreak> streaksNoFMC = FXCollections.observableArrayList();


    private void setTables() {

        TreeMap<Integer, Integer> tree = WCAReader.getYearTreeMap(Main.comps);
        yearCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().toString()));
        compCol.setCellValueFactory(cellData -> new SimpleStringProperty(tree.get(cellData.getValue()).toString()));
        daysBetweenCol.setCellValueFactory(cellData -> new SimpleStringProperty(getDaysBetweenComps(cellData.getValue(), tree.get(cellData.getValue()))));
        ObservableList<Integer> years = FXCollections.observableArrayList();
        years.addAll(tree.keySet());
        compYearTable.setItems(years);

        StreakCalculator calc = new StreakCalculator(Main.comps);

        streaks.addAll(calc.getBestStreaks(10, false));
        streaksNoFMC.addAll(calc.getBestStreaks(10, true));

        lengthCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getLength())));
        startCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStart()));
        endCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEnd()));
        pbStreakTable.setItems(streaks);
    }

    public void onExcludeFMCCheck() {
        if (excludeFMCCheck.isSelected()) {
            pbStreakTable.setItems(streaks);
        } else {
            pbStreakTable.setItems(streaksNoFMC);
        }
    }

    private String getDaysBetweenComps(Integer year, Integer comps) {
        int daysInYear = (year % 4 == 0 && year % 1000 != 0) ? 366 : 365;
        double result = (daysInYear - comps) / comps.doubleValue();
        result = (Math.round(100 * result) / 100.0);
        return String.valueOf(result);
    }

    public void setStage(Stage stage) {
        thisStage = stage;
    }

    @FXML
    public void returnToStart() {
        thisStage.close();
        Main.mainStage.show();
    }
}
