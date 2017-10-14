package me.matthewmcmillan.wcaanalyzer;


public abstract class Result implements Comparable<Result> {
    String round;
    Competition comp;
    Event event;
    boolean DNF = false, DNS = false;

    public Result(String resultString, Competition comp, Event event, String round) {
        this.comp = comp;
        this.round = round;
        this.event = event;

        if (resultString.equals("DNF")) {
            DNF = true;
        } else if (resultString.equals("DNS")) {
            DNS = true;
        } else {
            customParseResultString(resultString);
        }
    }

    // increments number of pbs in event
    public void setPbIfCompleted() {
        if (!isDNS() && !isDNF()) {
            event.incrementPbsBy(1);
            Main.events.get(event.getName()).incrementPbsBy(1);
        }
    }

    abstract double toGraphableValue();

    public Double getGraphableValue() {
        if (DNF || DNS) {
            return null;
        } else {
            return this.toGraphableValue();
        }
    }

    abstract void customParseResultString(String resultString);

    public String getRound() {
        return round;
    }

    public Competition getComp() {
        return comp;
    }

    abstract int customCompareTo(Result other);

    @Override
    public int compareTo(Result other) {
        if (other == null) {
            return -1;
        } else if ((DNF || DNS) && !(other.isDNF() || other.isDNS())) {
            return 1;
        } else if (!(DNF || DNS) && (other.isDNF() || other.isDNS())) {
            return -1;
        } else if ((DNS && other.isDNS()) || (DNF && other.isDNF())) {
            return 0;
        } else if (DNS && other.isDNF()) {
            return 1;
        } else if (DNF && other.isDNS()) {
            return -1;
        } else {
            return customCompareTo(other);
        }
    }

    abstract String customToString();

    @Override
    public String toString() {
        if (DNF) {
            return "DNF";
        } else if (DNS) {
            return "DNS";
        } else {
            return customToString();
        }
    }

    public boolean isDNF() {
        return DNF;
    }

    public boolean isDNS() {
        return DNS;
    }
}
