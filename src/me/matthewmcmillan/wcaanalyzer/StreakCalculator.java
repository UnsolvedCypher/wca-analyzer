package me.matthewmcmillan.wcaanalyzer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class StreakCalculator {
    private ArrayList<Competition> competitions;
    public StreakCalculator(ArrayList<Competition> competitions) {
        this.competitions = competitions;
    }

    public ArrayList<PBStreak> getBestStreaks(int numToGet, boolean excludeFMC) {
        ArrayList<PBStreak> streaksInOrder = getStreaksInOrder(excludeFMC);
        ArrayList<PBStreak> bestStreaks = new ArrayList<>(streaksInOrder);
        if (numToGet > streaksInOrder.size()) {
            numToGet = streaksInOrder.size();
        }
        Collections.sort(bestStreaks);
        Collections.reverse(bestStreaks);
        return new ArrayList<>(bestStreaks.subList(0, numToGet));
    }

    public PBStreak getLatestStreak(boolean excludeFMC) {
        ArrayList<PBStreak> streaksInOrder = getStreaksInOrder(excludeFMC);
        if (streaksInOrder.size() != 0) {
            return streaksInOrder.get(streaksInOrder.size() - 1);
        } else {
            return null;
        }
    }

    // set pbs for all events and competitions
    public static void calculatePbs() {
        for (Event event : Main.events.values()) {
            Result bestSingle = null;
            Result bestAverage = null;
            //System.out.println("There are " + event.getAttemptSequences().size() + " attempt sequences in " + event.getName());
            for (AttemptSequence sequence : event.getAttemptSequences()) {
                // there will never be a null single
                if (bestSingle == null) {
                    bestSingle = sequence.getSingle();
                    sequence.getSingle().setPbIfCompleted();
                } else {
                    if (bestSingle.compareTo(sequence.getSingle()) >= 0) {
                        bestSingle = sequence.getSingle();
                        sequence.getSingle().setPbIfCompleted();
                    }
                }
                // there could be a null average if the person didn't make the cutoff
                if (bestAverage == null && sequence.getAverage() != null) {
                    bestAverage = sequence.getAverage();
                    sequence.getAverage().setPbIfCompleted();
                } else if (sequence.getAverage() != null) {
                    if (bestAverage.compareTo(sequence.getAverage()) >= 0) {
                        bestAverage = sequence.getAverage();
                        sequence.getAverage().setPbIfCompleted();
                    }
                }

            }

        }

    }

    public ArrayList<PBStreak> getStreaksInOrder(boolean excludeFMC) {
        HashMap<String, Result> singlePBs = new HashMap<>();
        HashMap<String, Result> averagePBs = new HashMap<>();
        ArrayList<PBStreak> streaks = new ArrayList<>();
        boolean streakGoing = false;
        for (Competition comp : competitions) {
            if (!excludeFMC || (excludeFMC && !(comp.getEvents().size() == 1 && comp.getEvents().get(0).getName().equals("3x3x3 Fewest Moves")))) {
                // if a pb is broken at the comp
                if (comp.getPbs() > 0) {
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
        }
        return streaks;
    }
}
