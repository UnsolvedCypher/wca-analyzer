package me.matthewmcmillan.wcaanalyzer;

public class FMCResult implements Result {
    public FMCResult(String resultString, String comp, String round) {
        this.comp = comp;
        this.round = round;
        if (resultString.equals("DNF")) {
            this.DNF = true;
        } else if (resultString.equals("DNS")) {
            this.DNF = true;
        } else {
            this.moves = Double.parseDouble(resultString);
        }
    }

    String comp, round;

    private double moves;

    private boolean DNF, DNS;

    @Override
    public String getComp() {
        return comp;
    }

    @Override
    public String getRound() {
        return round;
    }

    public boolean isDNF() {
        return DNF;
    }

    public boolean isDNS() {
        return DNS;
    }

    @Override
    public String toString() {
        if (DNF) {
            return "DNF";
        } else if (DNS) {
            return "DNS";
        } else {
            if (moves % 1 == 0) {
                return String.valueOf((int)moves);
            } else {
                return String.valueOf(moves);
            }
        }
    }


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
            return new Double(this.moves).compareTo(new Double(((FMCResult)other).moves));
        }
    }
}
