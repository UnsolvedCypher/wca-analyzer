package me.matthewmcmillan.wcaanalyzer;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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

    public Tab getTab(Event event) {
        tab.setText(event.getName());
        singleTimeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().toString()));
        countingTimeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().toString()));
        averageTimeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().toString()));

        singleCompCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getComp()));
        countingCompCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getComp()));
        averageCompCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getComp()));

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
        singleSuccesses.setText("Single Successes: " + event.getSuccessfulSingles().size());
        averageSuccesses.setText("Average Successes: " + event.getNumNonDNFAverages());

        singleDNFBar.setProgress(event.getSingleDNFRate());
        averageDNFBar.setProgress(event.getAverageDNFRate());

        if (event.getName().contains("Blindfolded") || event.getName().contains("6x6") || event.getName().contains("7x7")) {
            ((HBox)countingTable.getParent().getParent()).getChildren().remove(1);
        }

        return tab;
    }
}
