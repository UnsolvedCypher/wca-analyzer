package me.matthewmcmillan.wcaanalyzer;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Date;
import java.util.TreeMap;

/**
 * Created by Matthew on 12/23/2016.
 */
public class GeneralController {
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
    public Tab getTab() {
        TreeMap<Integer, Integer> tree = WCAReader.getYearTreeMap(Main.comps);
        yearCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().toString()));
        compCol.setCellValueFactory(cellData -> new SimpleStringProperty(tree.get(cellData.getValue()).toString()));
        daysBetweenCol.setCellValueFactory(cellData -> new SimpleStringProperty(getDaysBetweenComps(cellData.getValue(), tree.get(cellData.getValue()))));
        ObservableList<Integer> years = FXCollections.observableArrayList();
        years.addAll(tree.keySet());
        compYearTable.setItems(years);


        StreakCalculator calc = new StreakCalculator(Main.comps);

        ObservableList<PBStreak> streaks = FXCollections.observableArrayList();
        streaks.addAll(calc.getBestStreaks(10));
        lengthCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getLength())));
        startCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStart()));
        endCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEnd()));
        pbStreakTable.setItems(streaks);

        return tab;
    }

    private String getDaysBetweenComps(Integer year, Integer comps) {
        int daysInYear = (year % 4 == 0 && year % 1000 != 0) ? 366 : 365;
        double result = (daysInYear - comps) / comps.doubleValue();
        result = (Math.round(100 * result) / 100.0);
        return String.valueOf(result);
    }
}
