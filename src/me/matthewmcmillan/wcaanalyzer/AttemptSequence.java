package me.matthewmcmillan.wcaanalyzer;

import java.util.ArrayList;
import java.util.Collections;

public class AttemptSequence {
    public AttemptSequence(String eventName, String round, Competition comp, int place, ArrayList<String> rawResults, String average) {
        this.round = round;
        this.comp = comp;
        this.place = place;
        this.eventName = eventName;
        this.results = parseRawResults(rawResults, comp, round);
        if (!average.equals("")) {
            this.average = new NormalResult(average, comp, round);
        }
    }

    private String round, eventName;
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

    public Result getAverage() {
        return average;
    }

    private ArrayList<Result> parseRawResults(ArrayList<String> rawResults, Competition comp, String round) {
        ArrayList<Result> parsedResults = new ArrayList<>();
        for (String result : rawResults) {
            if (eventName.equals("3x3x3 Fewest Moves")) {
                parsedResults.add(new FMCResult(result, comp, round));
            } else if (eventName.equals("3x3x3 Multi-Blind")) {
                parsedResults.add(new MultiResult(result, comp, round));
            } else {
                parsedResults.add(new NormalResult(result, comp, round));
            }
        }
        return parsedResults;
    }
}
