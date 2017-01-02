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
    VBox statsContent, graphsContent;

    @FXML
    ToggleButton statsButton1, statsButton2, graphsButton1, graphsButton2;

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
            initializeChart();
            switchToStats();
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

        private void initializeChart() {

        TreeMap<Integer, Integer> yearsAndComps = WCAReader.getYearTreeMap(Main.comps);
        Integer minYear = new ArrayList<Integer>(yearsAndComps.keySet()).get(0);
        Integer maxYear = new ArrayList<Integer>(yearsAndComps.keySet()).get(yearsAndComps.size() - 1);
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
        tab.setContent(statsContent);
        statsButton2.setSelected(false);
        graphsButton2.setSelected(true);
        statsButton1.requestFocus();
    }

    public void switchToGraphs() {
        tab.setContent(graphsContent);
        statsButton1.setSelected(true);
        graphsButton2.requestFocus();
        graphsButton1.setSelected(false);
    }
}
