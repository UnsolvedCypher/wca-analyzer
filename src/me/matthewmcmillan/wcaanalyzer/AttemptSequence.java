package me.matthewmcmillan.wcaanalyzer;

import java.util.ArrayList;
import java.util.Collections;

public class AttemptSequence {
    public AttemptSequence(String round, Competition comp, Event event, int place, ArrayList<String> rawResults, String average) {
        this.round = round;
        this.comp = comp;
        this.place = place;
        this.results = parseRawResults(rawResults, comp, event, round);
        // if there's no average, average will be null
        if (!average.equals("")) {
            this.average = new NormalResult(average, comp, event, round);
        }
    }

    private String round;
    private Competition comp;
    private int place;
    Result average;
    private ArrayList<Result> results;

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

    public Result getSingle() {
        ArrayList<Result> sortedResults = new ArrayList<Result>(results);
        Collections.sort(sortedResults);
        return sortedResults.get(0);
    }

    public Result getAverage() {
        return average;
    }

    private ArrayList<Result> parseRawResults(ArrayList<String> rawResults, Competition comp, Event event, String round) {
        ArrayList<Result> parsedResults = new ArrayList<>();
        for (String result : rawResults) {
            if (event.getName().equals("3x3x3 Fewest Moves")) {
                parsedResults.add(new FMCResult(result, comp, event, round));
            } else if (event.getName().equals("3x3x3 Multi-Blind")) {
                parsedResults.add(new MultiResult(result, comp, event, round));
            } else {
                parsedResults.add(new NormalResult(result, comp, event, round));
            }
        }
        return parsedResults;
    }

    @Override
    public String toString() {
        String str = "";
        for (Result result : results) {
            str += result.toString() + " ";
        }
        return str;
    }
}
