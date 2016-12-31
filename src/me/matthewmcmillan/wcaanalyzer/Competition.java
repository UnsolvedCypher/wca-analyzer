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
    private String name, url;
    private HashMap<String, Event> events = new HashMap<>();
    private LocalDate date;
    public void addAttemptSequence(String eventName, AttemptSequence sequence) {
        if (events.get(eventName) == null) {
            events.put(eventName, new Event(eventName));
        }
        events.get(eventName).addAttemptSequence(sequence);
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
