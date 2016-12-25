package me.matthewmcmillan.wcaanalyzer;

import java.util.ArrayList;
import java.util.Collections;

public class AttemptSequence {
    public AttemptSequence(String round, String comp, int place, String rawResults, String average) {
        this.round = round;
        this.comp = comp;
        this.place = place;
        this.results = parseRawResults(rawResults, comp, round);
        if (!average.equals(" ")) {
            this.average = new Result(average, comp, round);
        }
    }

    private String comp, round;
    private int place;
    Result average;
    private ArrayList<Result> results; // -1 is DNF, -2 is DNS

    public String getComp() {
        return comp;
    }

    public String getRound() {
        return round;
    }

    public int getPlace() {
        return place;
    }

    public ArrayList<Result> getResults() {
        return results;
    }

    public ArrayList<Result> getCountingTimes() {
        if (results.size() == 3) {
            return results;
        } else if (results.size() == 5) {
            ArrayList<Result> countingResults = new ArrayList<>();
            for (Result result : results) {
                if (!result.equals(Collections.max(results)) && !result.equals(Collections.min(results))) {
                    countingResults.add(result);
                }
            }
            return countingResults;
        } else {
            return new ArrayList<>();
        }
    }

    public Result getBest() {
        return Collections.max(results);
    }

    public Result getWorst() {
        return Collections.min(results);
    }

    public Result getAverage() {
        return average;
    }

    private static ArrayList<Result> parseRawResults(String rawResults, String comp, String round) {
        ArrayList<Result> parsedResults = new ArrayList<>();
        for (String result : rawResults.split(" " + (char)160 + " ")) {
            parsedResults.add(new Result(result, comp, round));
        }
        return parsedResults;
    }
}
