package me.matthewmcmillan.wcaanalyzer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PBStreak implements Comparable<PBStreak> {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
    public PBStreak(LocalDate start) {
        this.start = start;
    }
    public void increment() {
        length++;
    }

    public void setEnd(LocalDate endDate) {
        this.end = endDate;
    }

    @Override
    public String toString() {
        String endString = (end == null) ? "current" : formatter.format(end);
        return length + " competitions (" + formatter.format(start) + " - " + endString + ")";
    }

    private LocalDate start, end;
    public int length = 1;

    public String getEnd() {
        return (end == null) ? "current" : formatter.format(end);
    }

    public int getLength() {
        return length;
    }

    public String getStart() {

        return formatter.format(start);
    }

    @Override
    public int compareTo(PBStreak other) {
        return ((Integer)length).compareTo(other.length);
    }
}
