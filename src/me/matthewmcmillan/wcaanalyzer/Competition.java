package me.matthewmcmillan.wcaanalyzer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Competition implements Comparable<Competition> {
    public String getName() {
        return name;
    }

    public Competition(String name, LocalDate date) {
        this.name = name;
        this.date = date;
    }
    private String name;
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
