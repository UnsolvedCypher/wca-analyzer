package me.matthewmcmillan.wcaanalyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class StreakCalculator {
    private ArrayList<PBStreak> streaksInOrder;
    private boolean streakCurrent;
    public StreakCalculator(ArrayList<Competition> unorderedComps) {
        // not touching comps so the original arraylist isn't modified
        ArrayList<Competition> compsInOrder = new ArrayList<>(unorderedComps);
        // put comps in chronological order
        Collections.sort(compsInOrder);
        HashMap<String, Result> singlePBs = new HashMap<>();
        HashMap<String, Result> averagePBs = new HashMap<>();
        ArrayList<PBStreak> streaks = new ArrayList<>();
        boolean streakGoing = false;
        for (Competition comp : compsInOrder) {
            boolean pbBroken = false;
            for (Event event : comp.getEvents()) {
                if (event.getSuccessfulSingles().size() > 0 && event.getTopSingles(1).get(0).compareTo(singlePBs.get(event.getName())) <= 0) {
                    singlePBs.put(event.getName(), event.getTopSingles(1).get(0));
                    //System.out.println("broke single pb in " + event.getName());
                    pbBroken = true;
                } if (event.getNonDNFAverages().size() > 0 && event.getTopAverages(1).get(0).compareTo(averagePBs.get(event.getName())) <= 0) {
                    //System.out.println("broke average pb in " + event.getName() + " old was " + averagePBs.get(event.getName()) + " new: " +  event.getTopSingles(1).get(0));
                    averagePBs.put(event.getName(), event.getTopAverages(1).get(0));
                    pbBroken = true;
                }
            }
            if (pbBroken) {
                if (streakGoing) {
                    streaks.get(streaks.size() - 1).increment();
                } else {
                    streaks.add(new PBStreak(comp.getDate()));
                }
                streakGoing = true;
            } else if (streakGoing) {
                streakGoing = false;
                streaks.get(streaks.size() - 1).setEnd(comp.getDate());
            }
        }
        streakCurrent = streakGoing;
        this.streaksInOrder = streaks;

    }
    public ArrayList<PBStreak> getBestStreaks(int numToGet) {
        ArrayList<PBStreak> bestStreaks = new ArrayList<>(streaksInOrder);
        if (numToGet > streaksInOrder.size()) {
            numToGet = streaksInOrder.size();
        }
        Collections.sort(bestStreaks);
        Collections.reverse(bestStreaks);
        return new ArrayList<>(bestStreaks.subList(0, numToGet));
    }
    public PBStreak getLatestStreak() {
        if (streaksInOrder.size() != 0) {
            return streaksInOrder.get(streaksInOrder.size() - 1);
        } else {
            return null;
        }
    }
}
