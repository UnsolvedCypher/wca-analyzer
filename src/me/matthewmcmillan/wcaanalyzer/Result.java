package me.matthewmcmillan.wcaanalyzer;

public abstract class Result implements Comparable<Result> {
    public Result(String resultString, Competition comp, String round) {
        this.comp = comp;
        this.round = round;

        if (resultString.equals("DNF")) {
            DNF = true;
        } else if (resultString.equals("DNS")) {
            DNS = true;
        } else {
            customParseResultString(resultString);
        }
    }
    String round;

    abstract void customParseResultString(String resultString);

    Competition comp;

    public String getRound() {
        return round;
    }

    public Competition getComp() {
        return comp;
    }

    boolean DNF = false, DNS = false;

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
