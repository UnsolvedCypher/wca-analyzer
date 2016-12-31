package me.matthewmcmillan.wcaanalyzer;

import java.util.ArrayList;
import java.util.Collections;

public class Event {

    public Event(String name) {
        this.name = name;
    }
    private ArrayList<AttemptSequence> attemptSequences = new ArrayList<>();
    public void addAttemptSequence(AttemptSequence attemptSequence) {
        this.attemptSequences.add(attemptSequence);
    }
    public int getNumRoundsCompetedIn() {
        return attemptSequences.size();
    }

    public ArrayList<AttemptSequence> getAttemptSequences() {
        return attemptSequences;
    }

    public int getNumNonDNFAverages() {
        return getNonDNFAverages().size();
    }

    private String name;

    public ArrayList<Result> getTopCountingSingles(int toGet) {
        ArrayList<Result> countingTimes = new ArrayList<>();
        for (AttemptSequence sequence : attemptSequences) {
            countingTimes.addAll(sequence.getCountingTimes());
        }
        Collections.sort(countingTimes);
        if (toGet > countingTimes.size()) {
            toGet = countingTimes.size();
        }
        return new ArrayList<>(countingTimes.subList(0, toGet));
    }

    public String getName() {
        return name;
    }

    public ArrayList<Result> getNonDNFAverages() {
        ArrayList<Result> nonDNFAverages = new ArrayList<>();
        for (Result average : getAllAverages()) {
            if (!average.isDNF()) {
                nonDNFAverages.add(average);
            }
        }
        return nonDNFAverages;
    }

    public int getNumAverages() {
        return getAllAverages().size();
    }

    public ArrayList<Result> getTopSingles(int numOfTimes) {
        ArrayList<Result> allResults = getAllSingles();
        Collections.sort(allResults);

        if (numOfTimes > allResults.size()) {
            numOfTimes = allResults.size();
        }
        return new ArrayList<Result> (allResults.subList(0, numOfTimes));
    }

    public ArrayList<Result> getTopAverages(int numOfTimes) {
        ArrayList<Result> allResults = getAllAverages();
        Collections.sort(allResults);

        if (numOfTimes > allResults.size()) {
            numOfTimes = allResults.size();
        }
        return new ArrayList<Result> (allResults.subList(0, numOfTimes));
    }

    // percent DNFs out of all attempted solves (excluding DNS)
    public double getSingleDNFRate() {
        int DNFs = 0;
        for (Result single : getAllSingles()) {
            if (single.isDNF()) {
                DNFs++;
            }
        }
        return (double)DNFs / (double)(DNFs + getNonDNFSingles().size());
    }

    // no DNF or DNS
    public ArrayList<Result> getNonDNFSingles() {
        ArrayList<Result> successfulSolves = new ArrayList<>();
        for (Result result : getAllSingles()) {
            if (!result.isDNF() && !result.isDNS()) {
                successfulSolves.add(result);
            }
        }
        return successfulSolves;
    }

    // percent DNFs out of all completed averages
    public double getAverageDNFRate() {
        return (double)(getNumAverages() - getNumNonDNFAverages()) / (double) getNumAverages();
    }

    public int getNumAttempts() {
        return getAllSingles().size();
    }

    private ArrayList<Result> getAllSingles() {
        ArrayList<Result> allResults = new ArrayList<>();
        for (AttemptSequence attemptSequence : this.attemptSequences) {
            allResults.addAll(attemptSequence.getResults());
        }
        return allResults;
    }

    private ArrayList<Result> getAllAverages() {
        ArrayList<Result> allAverages = new ArrayList<>();
        for (AttemptSequence sequence : this.attemptSequences) {
            if (sequence.getAverage() != null) {
                allAverages.add(sequence.getAverage());
            }
        }
        return allAverages;
    }
}
