//package me.matthewmcmillan.wcaanalyzer;
//
//import javafx.fxml.FXML;
//import javafx.scene.chart.NumberAxis;
//import javafx.scene.chart.ScatterChart;
//import javafx.scene.chart.XYChart;
//
//import java.util.ArrayList;
//import java.util.TreeMap;
//
//public class GraphTabController {
//    @FXML
//    ScatterChart graph;
//
//    @FXML
//    public void initialize() {
//        TreeMap<Integer, Integer> yearsAndComps = WCAReader.getYearTreeMap(Main.comps);
//        Integer minYear = new ArrayList<Integer>(yearsAndComps.keySet()).get(0);
//        Integer maxYear = new ArrayList<Integer>(yearsAndComps.keySet()).get(yearsAndComps.size() - 1);
//        Integer maxComps = 0;
//        for (Integer comps : yearsAndComps.values()) {
//            if (comps > maxComps) {
//                maxComps = comps;
//            }
//        }
//
//        NumberAxis yearsAndCompsX = new NumberAxis(minYear, maxYear, 1);
//        NumberAxis yearsAndCompsY = new NumberAxis(0, maxComps, 5);
//
//        XYChart.Series yearsAndCompsSeries = new XYChart.Series();
//        for (Integer year : yearsAndComps.values()) {
//            yearsAndCompsSeries.getData().add(new XYChart.Data(year, yearsAndComps.get(year)));
//        }
//
//        graph.getData().addAll(yearsAndCompsSeries);
//
//
//    }
//}
