package me.matthewmcmillan.wcaanalyzer;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

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

    @FXML
    VBox statsContent, graphsContent;

    @FXML
    ToggleButton statsButton1, statsButton2, graphsButton1, graphsButton2;

    @FXML
    Slider startYear, endYear, startResult, endResult;

    private NumberAxis singleDateAxis, averageDateAxis, singleAxis, averageAxis;

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

        if (event.getName().contains("Blind") || event.getName().contains("6x6") || event.getName().contains("7x7")) {
            ((HBox)countingTable.getParent().getParent()).getChildren().remove(1);
        }

        //initializeChart();
        initializeGraphs(event);
        initializeSliders(event);
        switchToStats();
        return tab;
    }

    private void initializeGraphs(Event event) {


        if (!event.getName().contains("Multi")) {
            maxResult = event.getWorstSingles(1).size() > 0 ? event.getWorstSingles(1).get(0).toGraphableValue() : 0;
        } else {
            maxResult = event.getTopSingles(1).size() > 0 ? event.getTopSingles(1).get(0).toGraphableValue() : 0;
        }

        maxResult = Math.ceil(maxResult);

        XYChart.Series<Long, Double> singles = new XYChart.Series<>();
        XYChart.Series<Long, Double> averages = new XYChart.Series<>();

        for (Result single : event.getNonDNFSingles()) {
            singles.getData().addAll(new XYChart.Data<>(single.getComp().getDate().toEpochDay(), single.getGraphableValue()));
        }

        for (Result average : event.getNonDNFAverages()) {
            averages.getData().addAll(new XYChart.Data<>(average.getComp().getDate().toEpochDay(), average.getGraphableValue()));
        }

        singleDateAxis = new NumberAxis(minDate, maxDate, 365.25);
        averageDateAxis = new NumberAxis(minDate, maxDate, 365.25);
        singleAxis = new NumberAxis(0, Math.ceil(maxResult), Math.ceil(maxResult / 10));
        averageAxis = new NumberAxis(0, Math.ceil(maxResult), Math.ceil(maxResult / 10));

        if (!event.getName().contains("Multi") && !event.getName().equals("3x3x3 Fewest Moves")) {
            singleAxis.setTickLabelFormatter(NormalResult.getStringConverter());
            averageAxis.setTickLabelFormatter(NormalResult.getStringConverter());
        }


        singleDateAxis.setTickLabelFormatter(Main.dateStringConverter);
        averageDateAxis.setTickLabelFormatter(Main.dateStringConverter);

        ScatterChart singleGraph = new ScatterChart(singleDateAxis, singleAxis);
        ScatterChart averageGraph = new ScatterChart(averageDateAxis, averageAxis);

        singleGraph.setLegendVisible(false);
        averageGraph.setLegendVisible(false);

        singleGraph.setTitle("Singles");
        averageGraph.setTitle("Averages");

        HBox.setHgrow(singleGraph, Priority.ALWAYS);
        HBox.setHgrow(averageGraph, Priority.ALWAYS);


        singleGraph.getData().addAll(singles);
        // adding an empty series so that the chart changes color
        averageGraph.getData().add(new XYChart.Series<>());
        averageGraph.getData().add(new XYChart.Series<>());
        averageGraph.getData().addAll(averages);

        graphParent.getChildren().add(singleGraph);
        graphParent.getChildren().add(averageGraph);


    }

    private double maxResult;

    private double minDate = LocalDate.of(Main.comps.get(0).getDate().getYear(), Month.JANUARY, 1).toEpochDay();
    private double maxDate = LocalDate.of(Main.comps.get(Main.comps.size() - 1).getDate().getYear() + 1, Month.JANUARY, 1).toEpochDay();

    public void initializeSliders(Event event) {
        startYear.setLabelFormatter(Main.doubleDateStringConverter);
        endYear.setLabelFormatter(Main.doubleDateStringConverter);
        if (!event.getName().contains("Multi") && !event.getName().equals("3x3x3 Fewest Moves")) {
            startResult.setLabelFormatter(NormalResult.getDoubleStringConverter());
            endResult.setLabelFormatter(NormalResult.getDoubleStringConverter());
        }

        startYear.setMin(minDate);
        startYear.setMax(maxDate);
        startYear.setMajorTickUnit(365.25);
        startYear.setValue(minDate);
        endYear.setMin(minDate);
        endYear.setMax(maxDate);
        endYear.setMajorTickUnit(365.25);
        endYear.setValue(maxDate);
        double majorTickUnit = Math.ceil(maxResult / 4) == 0 ? 1 : Math.ceil(maxResult / 4);
        startResult.setMajorTickUnit(majorTickUnit);
        startResult.setMinorTickCount((int)majorTickUnit - 1);
        startResult.setMin(0);
        startResult.setMax(maxResult);
        startResult.setValue(0);
        endResult.setMajorTickUnit(majorTickUnit);
        endResult.setMinorTickCount((int)majorTickUnit - 1);
        endResult.setMin(0);
        endResult.setMax(maxResult);
        endResult.setValue(maxResult);

        setListener(startResult);
        setListener(endResult);
        setListener(startYear);
        setListener(endYear);
    }
    public void updateGraphs() {
        singleAxis.setLowerBound(startResult.getValue());
        averageAxis.setLowerBound(startResult.getValue());
        singleAxis.setUpperBound(endResult.getValue());
        averageAxis.setUpperBound(endResult.getValue());
        singleAxis.setTickUnit(Math.round((endResult.getValue() - startResult.getValue()) / 10));
        averageAxis.setTickUnit(Math.round((endResult.getValue() - startResult.getValue()) / 10));

        singleDateAxis.setLowerBound(startYear.getValue());
        averageDateAxis.setLowerBound(startYear.getValue());
        singleDateAxis.setUpperBound(endYear.getValue());
        averageDateAxis.setUpperBound(endYear.getValue());
        singleDateAxis.setTickUnit(365.25);
        averageDateAxis.setTickUnit(365.25);
    }

    private void setListener (Slider slider) {
        slider.valueProperty().addListener(e -> updateGraphs());
    }


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
