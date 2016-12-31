package me.matthewmcmillan.wcaanalyzer;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.Axis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.TreeMap;

public class EventTabController {
    private static final int NUM_RESULTS = 10;
    @FXML
    Tab tab;

    @FXML
    Label singleDNFLabel, averageDNFLabel, totalRounds, totalAttempts, singleSuccesses, averageSuccesses;

    @FXML
    ProgressBar singleDNFBar, averageDNFBar;

    @FXML
    TableView<Result> singleTable, countingTable, averageTable;

    @FXML
    TableColumn<Result, String> singleTimeCol, singleCompCol, countingTimeCol, countingCompCol, averageTimeCol, averageCompCol;

    @FXML
    HBox graphParent;

    public Tab getTab(Event event) {
        tab.setText(event.getName());
        singleTimeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().toString()));
        countingTimeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().toString()));
        averageTimeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().toString()));

        singleCompCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getComp().getName()));
        countingCompCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getComp().getName()));
        averageCompCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getComp().getName()));

        ObservableList<Result> topSingles = FXCollections.observableArrayList(event.getTopSingles(NUM_RESULTS));
        ObservableList<Result> topCountingSingles = FXCollections.observableArrayList(event.getTopCountingSingles(NUM_RESULTS));
        ObservableList<Result> topAverages = FXCollections.observableArrayList(event.getTopAverages(NUM_RESULTS));

        singleTable.setItems(topSingles);
        countingTable.setItems(topCountingSingles);
        averageTable.setItems(topAverages);

        singleDNFLabel.setText("Single DNF Rate: " + WCAReader.asPercent(event.getSingleDNFRate()));
        averageDNFLabel.setText("Average DNF Rate: " + WCAReader.asPercent(event.getAverageDNFRate()));
        totalRounds.setText("Rounds Competed in: " + event.getNumRoundsCompetedIn());
        totalAttempts.setText("Total Attempts: " + event.getNumAttempts());
        singleSuccesses.setText("Single Successes: " + event.getNonDNFSingles().size());
        averageSuccesses.setText("Average Successes: " + event.getNumNonDNFAverages());

        singleDNFBar.setProgress(event.getSingleDNFRate());
        averageDNFBar.setProgress(event.getAverageDNFRate());

        if (event.getName().contains("Blindfolded") || event.getName().contains("6x6") || event.getName().contains("7x7")) {
            ((HBox)countingTable.getParent().getParent()).getChildren().remove(1);
        }

        //initializeChart();
        return tab;
    }

    private void initializeGraphs(Event event) {
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

        NumberAxis yearsAndCompsX = new NumberAxis(minYear, maxYear, 1);
        NumberAxis yearsAndCompsY = new NumberAxis(0, maxComps, 5);

        ScatterChart graph = new ScatterChart<Number, Number>(yearsAndCompsX, yearsAndCompsY);

        graphParent.getChildren().add(graph);

        XYChart.Series yearsAndCompsSeries = new XYChart.Series();
        for (Integer year : yearsAndComps.keySet()) {
            yearsAndCompsSeries.getData().add(new XYChart.Data(year, yearsAndComps.get(year)));
        }

//        graph.getXAxis().setAutoRanging(true);
//        graph.getYAxis().setAutoRanging(true);

        graph.getData().addAll(yearsAndCompsSeries);
        graph.setTitle("hi");
    }
}
