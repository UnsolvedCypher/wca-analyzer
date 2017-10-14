package me.matthewmcmillan.wcaanalyzer;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.TreeMap;

public class StatsViewController {
    private Stage thisStage;

    @FXML
    TabPane tabPane;

    @FXML
    VBox mainBox;

    @FXML
    VBox statsContent, graphsContent, tabContent;

    @FXML
    ToggleButton statsButton, graphsButton;

    @FXML
    Label competitorName, version;

    @FXML
    HBox graphParent;

    @FXML
    Tab tab;

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
    Label totalAttempts, successfulSingles, successfulAverages, totalComps, totalPbs;

    @FXML
    VBox bottomStats;

    @FXML
    public void initialize() {
        try {
            ToggleGroup toggleGroup = new ToggleGroup();
            statsButton.setToggleGroup(toggleGroup);
            graphsButton.setToggleGroup(toggleGroup);
            version.setText("Version " + Main.VERSION);
            competitorName.setText("Stats for " + Main.competitorName + " (" + Main.WCAID + ")");
            setTables();
            for (Event event : Main.events.values()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("EventsTabWithGraphs.fxml"));
                loader.load();
                EventTabController eventTabController = loader.getController();
                tabPane.getTabs().add(eventTabController.getTab(event));
            }
            tabPane.getTabs().remove(1);
            tabPane.getTabs().remove(1);
            initializeChart();
            switchToStats();
            setBottomStatistics();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ObservableList<PBStreak> streaks = FXCollections.observableArrayList();
    ObservableList<PBStreak> streaksNoFMC = FXCollections.observableArrayList();


    private void setBottomStatistics() {
        int totalAttemptsInt = 0, successfulSinglesInt = 0, successfulAveragesInt = 0,
                totalCompsInt = Main.comps.size(), totalPbsInt = 0;
        for (Event event : Main.events.values()) {
            totalAttemptsInt += event.getNumAttempts();
            successfulSinglesInt += event.getNonDNFSingles().size();
            successfulAveragesInt += event.getNumNonDNFAverages();
            totalPbsInt += event.getPbs();
        }
        totalAttempts.setText("Total attempts: " + totalAttemptsInt);
        successfulSingles.setText("Single successes: " + successfulSinglesInt);
        successfulAverages.setText("Average successes: " + successfulAveragesInt);
        totalComps.setText("Number of competitions: " + totalCompsInt);
        totalPbs.setText("Number of personal bests: " + totalPbsInt);
        mainBox.getChildren().remove(bottomStats);
    }

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
            pbStreakTable.setItems(streaksNoFMC);
        } else {
            pbStreakTable.setItems(streaks);
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

        private void initializeChart() {

        TreeMap<Integer, Integer> yearsAndComps = WCAReader.getYearTreeMap(Main.comps);
        Integer minYear = new ArrayList<>(yearsAndComps.keySet()).get(0);
        Integer maxYear = new ArrayList<>(yearsAndComps.keySet()).get(yearsAndComps.size() - 1);
        Integer maxComps = 0;
        for (Integer comps : yearsAndComps.values()) {
            if (comps > maxComps) {
                maxComps = comps;
            }
        }

        maxComps = (int)Math.round(maxComps * 1.2);

        NumberAxis yearsAndCompsX = new NumberAxis(minYear - 1, maxYear + 1, 1);
        NumberAxis yearsAndCompsY = new NumberAxis(0, maxComps, Math.ceil(maxComps / 5));

        yearsAndCompsX.setTickLabelFormatter(numberStringConverter);

        LineChart graph = new LineChart(yearsAndCompsX, yearsAndCompsY);

        graphParent.getChildren().add(graph);

        XYChart.Series yearsAndCompsSeries = new XYChart.Series();
        for (Integer year : yearsAndComps.keySet()) {
            yearsAndCompsSeries.getData().add(new XYChart.Data(year, yearsAndComps.get(year)));
        }
        HBox.setHgrow(graph, Priority.ALWAYS);

        graph.getData().addAll(yearsAndCompsSeries);
        graph.setLegendVisible(false);
        graph.setTitle("Competitions per Year");
    }

    StringConverter<Number> numberStringConverter = new StringConverter<Number>() {
        @Override
        public String toString(Number object) {
            return String.valueOf(object.intValue());
        }

        @Override
        public Number fromString(String string) {
            return Integer.parseInt(string);
        }
    };

    public void switchToStats() {
        statsButton.setSelected(true);
        if (!tabContent.getChildren().get(1).equals(statsContent)) {
            tabContent.getChildren().set(1, statsContent);
            tabContent.getChildren().add(bottomStats);
            bottomStats.setMaxHeight(200);
            bottomStats.setPrefHeight(200);
            VBox.setVgrow(tabContent.getChildren().get(1), Priority.ALWAYS);
        }
    }

    public void switchToGraphs() {
        graphsButton.setSelected(true);
        if (!tabContent.getChildren().get(1).equals(graphsContent)) {
            tabContent.getChildren().set(1, graphsContent);
            tabContent.getChildren().add(bottomStats);
            VBox.setVgrow(tabContent.getChildren().get(1), Priority.ALWAYS);
        }
    }
}
