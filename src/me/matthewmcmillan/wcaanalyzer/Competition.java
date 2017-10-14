package me.matthewmcmillan.wcaanalyzer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Competition implements Comparable<Competition> {
    public String getName() {
        return name;
    }

    public Competition(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public void calculateDate() {
        try {
            this.date = WCAReader.getDateFromCompURL(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getPbs() {
        int pbs = 0;
        for (Event event : events.values()) {
            pbs += event.getPbs();
        }
        return pbs;
    }
    private String name, url;
    private HashMap<String, Event> events = new HashMap<>();
    private LocalDate date;
    public void addAttemptSequence(String event, String round, Competition comp, int place, ArrayList<String> rawResults, String average) {
        if (events.get(event) == null) {
            events.put(event, new Event(event));
        }
        events.get(event).addReverseAttemptSequence(new AttemptSequence(round, comp, events.get(event), place, rawResults, average));
    }


    public LocalDate getDate() {
        return date;
    }

    public ArrayList<Event> getEvents() {
        return new ArrayList<>(events.values());
    }

    @Override
    public int compareTo(Competition other) {
        return date.compareTo(other.date);
    }
}
